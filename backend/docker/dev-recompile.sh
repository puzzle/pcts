#!/bin/sh
set -eu

cd /app/backend
classes=target/classes
sources=target/dev-recompile.sources

# keep the trigger file, deleting it counts as a trigger and restarts the app on an empty classpath
clear_classes() {
  if [ -d "$classes" ]; then
    find "$classes" -mindepth 1 ! -name .reloadtrigger -delete
  fi
}

exec 9>/tmp/dev-recompile.lock
flock 9

offline=-o
mkdir -p target
find src/main -type f | sort > "$sources.new"
if [ "${1:-}" = "--full" ]; then
  echo "dev-recompile: full compile"
  clear_classes
  offline=
elif [ -f "$sources" ] && [ -n "$(comm -23 "$sources" "$sources.new")" ]; then
  echo "dev-recompile: sources were removed, full compile"
  clear_classes
fi

/opt/mvnd/bin/mvnd -B -q $offline -Pdev compile
mv "$sources.new" "$sources"
touch "$classes/.reloadtrigger"
