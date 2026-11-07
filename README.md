# ![PCTS Logo](frontend/public/images/pcts-logo.svg)

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/puzzle/pcts/issues)
![GitHub contributors](https://img.shields.io/github/contributors/puzzle/pcts)
![GitHub last commit](https://img.shields.io/github/last-commit/puzzle/pcts)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
![GitHub stars](https://img.shields.io/github/stars/puzzle/pcts)

PCTS stands for Puzzle ECTS, inspired by the
[European Credit Transfer and Accumulation System](https://en.wikipedia.org/wiki/European_Credit_Transfer_and_Accumulation_System).

We use the PCTS points to track which employees have what
training, certificates, experience, further education and adjust the salaries
according to that.

![UI](pctsUI.png)

## Documentation

See our Backend documentation: [Backend](backend/README.md)

See our Frontend documentation: [Frontend](frontend/README.md)

See our Migration tool documentation: [Migration tool](migration/README.md)

## Packages

You can find the PCTS Docker images on the [Packages page of our GitHub repository](https://github.com/puzzle/pcts/packages).
There are separate images for the frontend and backend components.

Note: When running the backend image, make sure to specify a Spring Profile to configure the application correctly.

## Running the Application

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

**Migration Tool:**

```shell
  docker compose --profile migration up
```

## Changelog

See what’s new in the latest versions by checking the [CHANGELOG.md](https://github.com/puzzle/pcts/blob/main/CHANGELOG.md).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

[AGPL v3](LICENSE) @ Puzzle ITC
