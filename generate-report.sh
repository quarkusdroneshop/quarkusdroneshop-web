#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TARGET="$SCRIPT_DIR/target"
FAILSAFE_DIR="$TARGET/failsafe-reports"
SUREFIRE_DIR="$TARGET/surefire-reports"

if [ ! -d "$FAILSAFE_DIR" ]; then
  echo "[ERROR] failsafe-reports が見つかりません: $FAILSAFE_DIR"
  echo "  先に ./mvnw verify を実行してテストを走らせてください。"
  exit 1
fi

mkdir -p "$SUREFIRE_DIR"
cp "$FAILSAFE_DIR"/TEST-*.xml "$SUREFIRE_DIR/" 2>/dev/null || true

python3 "$SCRIPT_DIR/src/test/scripts/generate-report.py" "$TARGET" "quarkusdroneshop-web"

echo "[test-report] open target/test-report/index.html"
