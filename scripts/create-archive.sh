#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
OUTPUT=${1:-"$ROOT_DIR/../hidroponic-project.zip"}
cd "$ROOT_DIR"

git archive --format=zip --output="$OUTPUT" HEAD

cat <<MSG
Archive created at: $OUTPUT
MSG
