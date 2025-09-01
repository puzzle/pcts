# PCTS

Stay tuned for things to come :)

## Development

### Git Hooks

There are some hooks, which are strongly encouraged to use. Simply execute the following command:

```shell
  git config --local core.hooksPath .githooks/
```

### Formatting

We use [markdownlint](https://github.com/markdownlint/markdownlint)for formating markdown files
and [yamllint](https://github.com/adrienverge/yamllint) for formating yaml and yml files.

We excluded the markdown rule `MD013` (line-length), because this rule does not work when the default value is changed.

#### Installation

Check the official GitHub documentation for Installation:

- [markdownlint](https://github.com/markdownlint/markdownlint#installation)
- [yamllint](https://github.com/adrienverge/yamllint#installation)

#### Usage

##### yamllint

```shell
  find . \( -name "*.yaml" -o -name "*.yml" \) -not -path "./frontend/node_modules/*" | xargs yamllint
```

##### markdownlint

```shell
  find . -name "*.md" -not -path "./frontend/node_modules/*" -not -path "./.github/ISSUE_TEMPLATE/*" | xargs mdl
```
