#!/bin/sh
# Compiles the backend inside the dev container and, on success, touches the devtools trigger file so the app
# restarts exactly once per compile. Run by the 'docker compose up --watch' sync+exec rule on src changes, with
# --full at container start (also after a pom.xml change, compose restarts the container) and by hand:
#   docker compose exec pcts-backend dev-recompile --full
#
# The dev maven profile only compiles changed sources. A full compile (empty target/classes) is done when
#   - --full is passed (e.g. after changing a method signature or a constant other classes use)
#   - a file under src/main was deleted or renamed, so its stale class/resource doesn't stay on the classpath
set -eu

cd /app/backend
classes=target/classes
sources=target/dev-recompile.sources

# empties target/classes but keeps the trigger file, deleting it would count as a trigger and restart the app
# on an empty classpath
clear_classes() {
  if [ -d "$classes" ]; then
    find "$classes" -mindepth 1 ! -name .reloadtrigger -delete
  fi
}

# sync+exec runs once per change batch, don't let two compiles write into target/classes at the same time
exec 9>/tmp/dev-recompile.lock
flock 9

# offline unless it's a full compile, a pom.xml change can add dependencies
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

mvnd -B -q $offline -Pdev compile
mv "$sources.new" "$sources"
touch "$classes/.reloadtrigger"
