#!/usr/bin/env bash
# PR Comments with Last-Line Diff and Suggestions (Markdown Output)

set -e

read -p "Enter PR link (or owner/repo#number): " pr_input

# --- Parse owner/repo/PR number ---
if [[ "$pr_input" =~ github.com/([^/]+)/([^/]+)/pull/([0-9]+) ]]; then
    owner="${BASH_REMATCH[1]}"
    repo="${BASH_REMATCH[2]}"
    pr_number="${BASH_REMATCH[3]}"
elif [[ "$pr_input" =~ ([^/]+)/([^#]+)#([0-9]+) ]]; then
    owner="${BASH_REMATCH[1]}"
    repo="${BASH_REMATCH[2]}"
    pr_number="${BASH_REMATCH[3]}"
else
    echo "Invalid PR input"
    exit 1
fi

# --- Fetch unresolved review threads with line info ---
threads_json=$(gh api graphql -f query="
{
  repository(owner: \"$owner\", name: \"$repo\") {
    pullRequest(number: $pr_number) {
      reviewThreads(first: 50) {
        nodes {
          isResolved
          comments(first: 10) {
            nodes {
              body
              path
              line
              originalLine
              diffHunk
            }
          }
        }
      }
    }
  }
}")

# --- Step 4: Extract unresolved comments ---
comments_json=$(echo "$threads_json" | jq '[.data.repository.pullRequest.reviewThreads.nodes[]
  | select(.isResolved == false)
  | .comments.nodes[]
  | {path: .path, body: .body, line: .line, originalLine: .originalLine, diffHunk: .diffHunk}]')

files=$(echo "$comments_json" | jq -r '.[].path' | sort -u)

# --- Step 6: Print comments grouped by file in Markdown ---
echo "## Current tasks in: #$pr_number"
echo

for file in $files; do
    echo "#### \`$file\`"
    echo

    file_comments=$(echo "$comments_json" | jq --arg path "$file" -c '.[] | select(.path==$path)')

    echo "$file_comments" | while read -r comment; do
        body=$(echo "$comment" | jq -r '.body')
        diff=$(echo "$comment" | jq -r '.diffHunk')

        # --- Detect language for code block ---
        ext="${file##*.}"
        case "$ext" in
            java) lang="java" ;;
            ts) lang="ts" ;;
            js) lang="js" ;;
            py) lang="python" ;;
            sh) lang="bash" ;;
            sql) lang="sql" ;;
            yml) lang="yml" ;;
            yaml) lang="yaml" ;;
            *) lang="diff" ;;
        esac

    # Filter comments for this file
    file_comments=$(echo "$comments_json" | jq --arg path "$file" -c '.[] | select(.path==$path)')

        # --- Check for GitHub suggestion block ---
        if grep -q '```suggestion' <<< "$body"; then
            # Print the comment text first (everything outside suggestion blocks)
            echo
            echo "$body" | awk 'BEGIN{inside=0}
                /^```suggestion/{inside=1; next}
                /^```/{inside=0; next}
                inside==0 {print}' | sed '/^[[:space:]]*$/d'
            echo

            # Then print all suggestion blocks
            # Then print suggestion blocks using correct language
            echo "$body" | awk -v lang="$lang" '
                BEGIN {inside=0}
                /^```suggestion/ {inside=1; print "```" lang; next}
                /^```/ {inside=0; print "```"; next}
                inside==1 {print}'
        else
            # otherwise print the body as normal text
            echo "- [ ] $body"
            echo
            # --- Print last line of diffHunk ---
            last_line=$(echo "$diff" | tail -n 1)
            if [[ -n "$last_line" ]]; then
                echo '```'"$lang"
                # strip leading +, -, or space
                echo "${last_line:1}"
                echo '```'
            else
                echo '```'
                echo "// Not associated with specific code line"
                echo '```'
            fi
        fi
        echo
    done
    echo "---"
done

echo "_End of PR comments_"
