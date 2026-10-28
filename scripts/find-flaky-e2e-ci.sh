#!/bin/sh
# Lists Cypress tests of on-push.yaml e2e jobs that failed in attempt 1 but the run passed on re-run,
# grouped by test with occurrence count, errors and job URLs.
# Prerequisites: gh sed awk grep
# Usage: ./find-flaky-e2e-ci.sh [days=14] [repo=puzzle/pcts]

days=${1:-14}
repo=${2:-puzzle/pcts}
since=$(date -d "$days days ago" +%F)
tab=$(printf '\t')

# Turns a raw job log (stdin) into one "spec<TAB>test<TAB>error<TAB>url" line per failing test.
failing_tests() {
  sed -E -e 's/\r$//' -e 's/(\x1b|␛)\[[0-9;]*[A-Za-z]//g' -e 's/^\xEF\xBB\xBF//' \
         -e 's/^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9:.]+Z ?//' \
    | awk '/[0-9]+ failing *$/ {p=1} /\(Results\)/ {p=0} p' \
    | grep -vE '^ +at |^[[:space:]]*$' \
    | awk -v spec="$1" -v url="$2" '
        function flush() { if (title != "") print spec "\t" title "\t" err "\t" url; title = ""; err = ""; errlines = 0 }
        { line = $0; sub(/^ +/, "", line) }
        line ~ /^[0-9]+ failing/ { next }
        line ~ /^[0-9]+\) / { flush(); sub(/^[0-9]+\) /, "", line); title = line; intitle = 1
                              if (sub(/:$/, "", title)) intitle = 0; next }
        intitle { title = title " › " line; if (sub(/:$/, "", title)) intitle = 0; next }
        # Error lines are joined with \002 so a failure stays one record; capped so huge diffs stay readable.
        title != "" && errlines++ < 20 { err = (err == "" ? "" : err "\002") line }
        END { flush() }'
}

ids=$(gh run list -R "$repo" -w on-push.yaml --created ">=$since" -L 1000 \
  --json databaseId,attempt,conclusion \
  --jq '.[] | select(.attempt > 1 and .conclusion == "success") | .databaseId')

n=$(printf '%s' "$ids" | grep -c .)
echo "Found $n on-push.yaml runs since $since that were re-run and passed." >&2
echo "Checking attempt 1 of each for a failed 'Cypress run e2e tests' step ([checked/total] run <id>):" >&2
i=0
records=""
for id in $ids; do
  i=$((i + 1))
  echo "[$i/$n] run $id" >&2
  jobs=$(gh api "repos/$repo/actions/runs/$id/attempts/1/jobs?per_page=100" \
    --jq '.jobs[] | select((.name | startswith("e2e-tests / e2e")) and any(.steps[]; .name == "Cypress run e2e tests" and .conclusion == "failure")) | "\(.id)\t\(.name)\t\(.html_url)"')
  [ -z "$jobs" ] && continue

  while IFS="$tab" read -r job name url; do
    spec=$(printf '%s' "$name" | sed -E 's/.*\((.*)\)$/\1/')
    if log=$(gh api --allow-escape-sequences "repos/$repo/actions/jobs/$job/logs" 2>&1); then
      found=$(printf '%s\n' "$log" | failing_tests "$spec" "$url")
      [ -z "$found" ] && found="$spec${tab}(no failing block found in log)${tab}${tab}$url"
    else
      found="$spec${tab}(could not fetch log)${tab}$(printf '%s' "$log" | head -1)${tab}$url"
    fi
    records="$records$found
"
  done <<EOF
$jobs
EOF
done

printf '\n=== flaky e2e tests since %s ===\n' "$since"
printf '%s' "$records" | grep . | awk -F "$tab" '
  { key = $1 "\t" $2; count[key]++
    err = $3; gsub(/\002/, "\001        ", err)
    items[key] = items[key] "\001    " $4 (err == "" ? "" : "\001        " err) }
  END { for (k in count) { split(k, p, "\t"); printf "%d\t%dx  %s › %s%s\001\n", count[k], count[k], p[1], p[2], items[k] } }' \
  | sort -rn | sed 's/^[0-9]*\t//' | tr '\001' '\n'