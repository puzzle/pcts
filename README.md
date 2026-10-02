# PCTS

## Packages

You can find the PCTS Docker images on the [Packages page of our GitHub repository](https://github.com/puzzle/pcts/packages).
There are separate images for the frontend and backend components.

Note: When running the backend image, make sure to specify a Spring Profile to configure the application correctly.

## Development

### Prerequisites: pnpm

This project uses [pnpm](https://pnpm.io) as the frontend package manager. **npm and yarn are not supported** — the `preinstall` script will block them.

Install pnpm via [corepack](https://nodejs.org/api/corepack.html) (bundled with Node.js ≥ 16):

```shell
corepack enable
corepack prepare pnpm@10.32.1 --activate
```

Or via standalone install:

```shell
npm install -g pnpm@10.32.1
```

Then install frontend dependencies:

```shell
cd frontend && pnpm install
```

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

#### dev setup with auto reload

the default and the `backend` profile run the backend from source with spring devtools, start it with `--watch` so code changes get picked up:

```shell
  docker compose --profile backend up --watch
```

`up --wait` blocks until everything is healthy, every service waits for its dependencies to be healthy anyway (the first backend start does a full compile, so give it a minute).

#### how a change gets into the running app

save a file under `backend/src` -> compose syncs it into the container -> `dev-recompile` compiles it -> touches `target/classes/.reloadtrigger` -> devtools restarts the spring context.
devtools only watches the trigger file and not `target/classes` itself, so the app restarts exactly once per successful compile and never on a half written classpath. a failed compile doesn't touch the trigger file, the app keeps running on the last good classes.

the JVM itself keeps running during a restart, so an attached debugger stays attached. JDWP (java debug wire protocol, the port a remote debugger connects to for breakpoints and stepping) listens on `localhost:5005`, use the `Backend-Debug` run config in IntelliJ. it's bound to localhost only since whoever can reach that port can run code in the JVM.

#### dev-recompile

`backend/docker/dev-recompile.sh` is bind mounted to `/usr/local/bin/dev-recompile`, a change to it applies immediately.
the `dev` maven profile only compiles sources newer than their class file. this is fast but classes depending on a changed one are **not** recompiled, so after changing a method signature or a constant other classes use run a full compile by hand:

```shell
  docker compose exec pcts-backend dev-recompile --full
```

a deleted or renamed file under `src/main` triggers a full compile on its own, otherwise the stale class would stay on the classpath.

#### why mvnd

the recompile runs on [mvnd](https://github.com/apache/maven-mvnd), a maven daemon that stays warm between builds. plain `mvn` starts a new JVM, loads maven and all plugins and runs cold every time, which is most of the time for a small change. measured in the container with nothing to compile: `mvn compile -o` 13.3s, `dev-recompile` 1.9s.
the full compile at container start also warms up the daemon. the app itself runs on plain `mvn spring-boot:run` so it doesn't occupy the daemon.

there is no official mvnd image, so the Dockerfile downloads the release tarball in its own `mvnd` stage. it stays cached and is only downloaded again when `MVND_VERSION` changes.

#### pom.xml changes

a `pom.xml` change gets synced and restarts the container, the start command (`dev-recompile --full`) then does a full compile online and resolves new dependencies. all other recompiles run offline against the dependencies baked into the image.
a Dockerfile change needs `docker compose up --build`.

#### Dockerfile stages

- `build`: packages everything, the `MAVEN_PROFILES` build arg selects the maven profile (empty for release/CI, `dev` from compose)
- `mvnd`: downloads mvnd, independent of the build so a code change doesn't download it again
- `dev`: `build` + mvnd, used by compose. the whole dev setup (start command, devtools env vars, the `dev-recompile` mount) lives in `docker/docker-compose.yml`, not in the Dockerfile
- `runner`: default stage, the release image. only copies the extracted jar layers from `build`, used for releases and e2e (`docker-compose.e2e.yml`)

#### frontend

`pcts-frontend` runs `pnpm start` (ng serve with HMR) in a `node` container on the bind mounted `frontend/`, so there is nothing to watch or rebuild. `node_modules` and `.angular` live in named volumes, `/api` is proxied to the backend.
after a `package.json` or lockfile change run `docker compose restart pcts-frontend`.
