# Web マイクロサービス

## 概要

Web はドローンショップの **フロントエンド・注文受付サービス** です。

- ブラウザ UI からのドローン注文を受け付け
- REST API `/order` 経由でも注文可能
- Kafka へ注文を送信し、リアルタイムでステータス更新を受信
- ロイヤリティポイント更新の受信・表示

**フレームワーク**: Quarkus  
**デプロイ先クラスター**: a-cluster

---

## アーキテクチャ

```
ブラウザ / REST クライアント
        │
        ▼ HTTP POST /order
┌──────────────┐
│  Web サービス │──► Kafka: orders-in ──► Counter
│              │
│              │◄── Kafka: web-updates（注文ステータス）
│              │◄── Kafka: loyalty-updates（ポイント）
│              │◄── Kafka: shop-bsite-rewards（リワード）
└──────────────┘
```

### Kafka トピック一覧

| トピック | 方向 | 説明 |
|---------|------|------|
| `orders-in` | 送信 | Counter への新規注文 |
| `web-updates` | 受信 | 注文ステータス更新 |
| `loyalty-updates` | 受信 | ロイヤリティポイント更新 |
| `shop-bsite-rewards` | 受信 | リワード通知（b-cluster からのミラー） |

### REST API

| エンドポイント | メソッド | 説明 |
|--------------|--------|------|
| `/order` | GET | 現在の注文一覧取得 |
| `/order` | POST | 新規注文送信 |

---

## ローカル開発

### 前提条件

- Java 17+
- Docker / Docker Compose

### 1. インフラ起動

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-support.git
cd quarkusdroneshop-support
docker compose up -d
```

PostgreSQL・Kafka・Zookeeper が起動します。

### 2. アプリケーション起動

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-web.git
cd quarkusdroneshop-web
./mvnw clean compile quarkus:dev
```

ブラウザで http://localhost:8080 にアクセスして UI を確認できます。

### 環境変数

| 変数名 | デフォルト | 説明 |
|--------|-----------|------|
| `KAFKA_BOOTSTRAP_URLS` | `localhost:9092` | Kafka ブートストラップアドレス |


### テストフォルダ



### 注文送信テスト

```shell
curl -X POST http://localhost:8080/order \
  -H "Content-Type: application/json" \
  -d '{"item": "DRONE_A", "quantity": 1, "location": "HOME"}'
```

---

## 本番デプロイ（Tekton Pipeline）

### パイプライン概要

```
fetch-repository → semgrep-scan → maven-run → push-oc-apps
```

| ステップ | 内容 |
|---------|------|
| `fetch-repository` | GitHub からソースをクローン |
| `semgrep-scan` | SAST セキュリティスキャン（p/java, p/owasp-top-ten, p/secrets） |
| `maven-run` | `clean verify -Dquarkus.package.jar.type=uber-jar` |
| `push-oc-apps` | OpenShift a-cluster へビルド＆デプロイ |

### 手動実行

```shell
tkn pipeline start build-and-push-quarkusdroneshop-web \
  -n quarkusdroneshop-cicd \
  --use-param-defaults
```

RHDH の **CI タブ** からパイプライン実行状況をリアルタイムで確認できます。

---

## テスト

```shell
# ユニットテスト
./mvnw test

# 統合テスト（Jacoco、ArchUnit含む,PlayWright含む）
./mvnw verify

# チェックスタイル
./mvnw checkstyle:check

# PMD
./mvnw pmd:pmd

# SpotBugs
./mvnw spotbugs:spotbugs

# semgrep 
semgrep scan --config p/default --json > target/semgrep-results.json

# secret scan
gitleaks detect --source . --report-format json --report-path target/gitleaks-report.json --exit-code 1

# 脆弱性テスト
trivy fs --scanners vuln,secret,misconfig,license --exit-code=1 --ignorefile ./.trivyignore.yaml ./ > target/trivy.txt

# ペネトレーションテスト
mvn quarkus:dev > quarkus.log 2>&1 & QUARKUS_PID=$!; sleep 10; wapiti -u http://localhost:8080 -f json -o ./target/wapiti.json; kill $QUARKUS_PID

# テストレポートの作成
./mvnw exec:exec@generate-report

# Webテストのレコーダ
npx playwright codegen --target=java -o src/test/e2e/io/quarkusdroneshop/web/WebTest.java http://localhost:8080

# UI 確認（ローカル起動後）
open http://localhost:8080
```

---

## 注意事項
- **Server-Sent Events (SSE)**: ステータス更新は SSE でブラウザにプッシュ配信。ロードバランサーのタイムアウト設定（60秒以上）に注意。
- **Kafka トピック名**: `web-updates` は a-cluster のトピック。
- **Kafka トピック名**: `loyalty-updates` は a-cluster のトピック。
- **Kafka トピック名**: `shop-bsite-rewards` は b-cluster から MirrorMaker2 でミラーリングされたトピック。
- **CORS**: 本番環境では `quarkus.http.cors.origins` を適切に設定してください。

### Outgoing
mp.messaging.outgoing.orders-up.connector=smallrye-kafka
mp.messaging.outgoing.orders-up.value.serializer=org.apache.kafka.common.serialization.StringSerializer
%prod.mp.messaging.outgoing.orders-up.topic=orders-in
%dev.mp.messaging.outgoing.orders-up.topic=orders-in