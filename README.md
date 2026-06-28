# quarkusdroneshop-web

Quarkus ベースの Web フロントエンドサービス。注文受付 REST API と、注文ステータスをリアルタイムに通知する Server-Sent Events (SSE) ダッシュボードを提供します。

- **バージョン**: 5.2.0
- **Quarkus**: 3.36.3

## アーキテクチャ

```
ブラウザ
  │  POST /order または Web UI
  ▼
quarkusdroneshop-web ──▶ orders-in ──▶ quarkusdroneshop-counter
                     ◀── web-updates
                     ◀── loyalty-updates
                     ◀── rewards (shop-bsite.rewards in prod)
                     ──▶ /dashboard/stream (SSE) ──▶ ブラウザ
```

## エンドポイント

| パス | 説明 |
|---|---|
| `POST /order` | 注文受付 REST API |
| `GET /dashboard/stream` | SSE ストリーム (注文ステータス更新) |
| `GET /` | Web UI (注文フォーム + ダッシュボード) |

## Kafka トピック

| チャネル | dev トピック | prod トピック | 方向 |
|---|---|---|---|
| orders-up (送信) | `orders-in` | `orders-in` | 送信 |
| web-updates | `web-updates` | `web-updates` | 受信 |
| loyalty-updates | `loyalty-updates` | `loyalty-updates` | 受信 |
| rewards | `rewards` | `shop-bsite.rewards` | 受信 |

## ローカル開発

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-support.git
cd quarkusdroneshop-support
docker compose up
```

```shell
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
  STREAM_URL=http://localhost:8080/dashboard/stream \
  CORS_ORIGINS=http://localhost:8080 \
  STORE_ID=ATLANTA

./mvnw clean compile quarkus:dev
```

デバッガーポートを変更する場合:

```shell
./mvnw clean compile quarkus:dev -Ddebug=5006
```

## 環境変数

| 変数名 | 説明 |
|---|---|
| `KAFKA_BOOTSTRAP_URLS` | Kafka ブローカー URL |
| `STREAM_URL` | SSE エンドポイント URL (`/dashboard/stream`) |
| `CORS_ORIGINS` | CORS 許可オリジン |
| `STORE_ID` | 店舗識別子 (例: `ATLANTA`) |

## pgAdmin (開発環境)

docker-compose で pgAdmin4 が起動します:
- URL: `http://localhost:5050`
- ログイン: `quarkus.shop@redhat.com` / `redhat-20`
- DB接続: Host=`crunchy`, Port=`5432`, DB=`postgres`, User=`postgres`, PW=`redhat-20`

## パッケージング

```shell
# ネイティブビルド
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <REGISTRY>/quarkusdroneshop-web .

# 実行
docker run -i --network="host" \
  -e KAFKA_BOOTSTRAP_URLS=localhost:9092 \
  -e STREAM_URL=http://localhost:8080/dashboard/stream \
  -e CORS_ORIGINS=http://localhost:8080 \
  <REGISTRY>/quarkusdroneshop-web:latest
```

## 参考

- [Quarkus](https://quarkus.io/)
- [quarkusdroneshop.github.io](https://quarkusdroneshop.github.io)
