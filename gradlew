#!/usr/bin/env sh
set -eu
ROOT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
VERSION=9.3.1
CACHE="$ROOT/.gradle-local"
DIST="$CACHE/gradle-$VERSION-bin.zip"
HOME_DIR="$CACHE/gradle-$VERSION"
EXE="$HOME_DIR/bin/gradle"
if [ ! -x "$EXE" ]; then
  mkdir -p "$CACHE"
  if [ ! -f "$DIST" ]; then
    command -v curl >/dev/null 2>&1 || { echo "curl is required" >&2; exit 1; }
    echo "Downloading Gradle $VERSION..."
    curl -fL --retry 3 -o "$DIST" "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip"
  fi
  rm -rf "$CACHE/_extract"
  mkdir -p "$CACHE/_extract"
  command -v unzip >/dev/null 2>&1 || { echo "unzip is required" >&2; exit 1; }
  unzip -q "$DIST" -d "$CACHE/_extract"
  rm -rf "$HOME_DIR"
  mv "$CACHE/_extract/gradle-$VERSION" "$HOME_DIR"
  chmod +x "$EXE"
fi
exec "$EXE" "$@"
