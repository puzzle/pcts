# PCTS

Stay tuned for things to come :)

## Development

### Git Hooks

There are some hooks, which are strongly encouraged to use. Simply execute the following command:

```shell
  git config --local core.hooksPath .githooks/
```

### Formatting

#### Code Formatting

Refer to the Frontend and Backend Formatting Guides:

- [Frontend Formatting](frontend/README.md)
- [Backend Formatting](backend/README.md)

#### markdownlint and yamllint

We use [markdownlint](https://github.com/markdownlint/markdownlint) for formating markdown files
and [yamllint](https://github.com/adrienverge/yamllint) for formating yaml and yml files.

We've disabled the markdown `MD013` line-length rule. The default limit of 80 characters is too restrictive, and we're unable to change it to a more flexible value.

##### Installation

Check the official GitHub documentation for Installation:

- [markdownlint](https://github.com/markdownlint/markdownlint#installation)
- [yamllint](https://github.com/adrienverge/yamllint#installation)

##### Usage

If you run the following commands the complete project expect for `node_modules` and `ISSUE_TEMPLATE` is checked for markdown or yaml files.
If no output is seen, that means that the linters didn't find any issues.

The commands are also run automatic in the precommit hook.

###### yamllint

```shell
  yamllint .
```

###### markdownlint

```shell
  find . -name "*.md" -not -path "./frontend/node_modules/*" -not -path "./.github/ISSUE_TEMPLATE/*" | xargs mdl
```

### Docker

To start the application with Docker, navigate to the `/docker` directory.

There are different profiles available.

**Full-stack:**

```shell
  docker compose up
```

**Backend and DB:**

```shell
  docker compose --profile backend up
```

**Only DB:**

```shell
  docker compose --profile db up
```
