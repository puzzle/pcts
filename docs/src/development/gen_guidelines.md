# General Guidelines
Some general guidelines for contributing to this project.

## Code of Conduct
Check out our code of conduct [here](https://github.com/puzzle/pcts?tab=coc-ov-file).

## Git Messages
Use [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/#summary) with the following types: `build`, `chore`, `ci`, `docs`, `feat`, `fix`, `refactor`, `revert`, `style`, `test`

### Format
Each commit message should consist of a **type**, an optional **scope**, a **subject** and an optional **issue number**.
```
type(scope): subject #issue-number
```

### Types
The type should answer the Question 'What kind of change was made?', e.g. a fix or a new CI workflow.

The type must be one of the following:
- `feat`: A new feature in the software the user uses
- `fix`: A bug fix in the software the user uses
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, ...)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests
- `build`: Changes that affect the build system or external dependencies
- `ci`: Changes to CI configuration files and scripts
- `chore`: Other changes that do not modify source or test files
- `revert`: Reverts a previous commit

### Scopes
The scope should answer the question 'Where in the codebase was the change made?', e.g. in the API or in the authentication module. 

Scopes are more fluid than types and are thus not handled as strictly. Some example scopes are:
- `overview`: A specific part of the frontend
- `shared`: Shared services or components
- `api`: Changes in a DTO or controller
- `service`: Changes in some validation or business service
- `persistence`: Changes in a persistence service
- `db`: Anything related to database schemas or migrations
- `deps`: Dependency updates

### Examples
- `feat: Allow users to upload a profile picture`
- `fix(api): Correctly handle null values in user endpoint #123`
- `docs: Update README with setup instructions #45`
- `chore(deps): Upgrade project dependencies #78`

## Git Branching
- Branches should be written in lowercase characters, hyphens and numbers. 
- Each branch should also have a prefix like this `feature/`, `bug/`, ... .
- If a branch is connected to an issue, the issue number should be included after the prefix. For example `feature/1829-...` for the issue 1829.

### Prefix
The prefix should be one of the following:
- `feature`: A new feature in the software the user uses or everything that cannot be placed under another category
- `bug`: A bug fix in the software the user uses
- `docs`: Documentation only changes
- `renovate`: Reserved for the Renovate bot

### Examples
- `bug/1571-inconsistent-auto-calculation-on-metric-keyresults`
- `feature/18-create-member-view`
- `bug/remove-wrong-reference` (this branch does not have an associated ticket)
- `docs/12-add-missing-db-setup`

## Pull Requests (PR)
- **PR description:** Add any needed additional context to the PR description.
- **Mark your PR as a draft:** If the PR is not yet ready for a review, mark it as a draft.