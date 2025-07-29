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

### Type
The type must be one of the following:
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, ...)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests
- `build`: Changes that affect the build system or external dependencies
- `ci`: Changes to CI configuration files and scripts
- `chore`: Other changes that do not modify source or test files
- `revert`: Reverts a previous commit

### Examples
- `feat: Allow users to upload a profile picture`
- `fix(api): Correctly handle null values in user endpoint #123`
- `docs: Update README with setup instructions #45`
- `chore: Upgrade project dependencies #78`

## Pull Requests (PR)
- **PR description:** Add any needed additional context to the PR description.
- **Mark your PR as a draft:** If the PR is not yet ready for a review, mark it as a draft.