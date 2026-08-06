# PCTS Backend

The backend of this project is written in Spring Boot.

## Development

### Dev-Database

The database can be started via docker

- cd into the root directory of the project
- `cd docker`
- Run `docker compose up`

### Dev-Backend

Make sure to set up the DB first

#### Terminal

You can start the backend via Terminal

- cd to the project root
- `cd backend`
- `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"`

## Formatting

We use the ***spotless*** Plugin for formatting the Java code:
[https://github.com/diffplug/spotless](https://github.com/diffplug/spotless)

### Checking and applying formatting

- To check the code formatting run `mvn spotless:check`
- To then format the code run `mvn spotless:apply`

If you have not set up our [Git Hooks](../README.md#git-hooks) we strongly
encourage you to do so since they also contain a check for the formatting
whenever you commit.

### How to update the spotless configuration using Intellij IDEA

- Open `Go to Settings -> Editor -> Code styles -> Java`
- Select the default project config and export it to a file
- Then make the changes you want to the code style config. Make sure to
- ***HIT APPLY*** and then export it as a file
- Afterward run the following command
  `git --no-pager diff --no-index -U0 default.xml changed.xml | egrep '^\+' | diff-so-fancy`
  to see the changes
- Finally copy all additions to the formatter file in the backend project

## Api Documentation

We use the ***springdoc-openapi*** library for documenting the API.

### Accessing the Api Documentation

Our API documentation is generated using ***springdoc-openapi***.
You can access it through a web interface (Swagger UI) or as a
raw json file (OpenAPI 3 specification).

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [OpenAPI 3 specification](http://localhost:8080/v3/api-docs)

## CertificateType Link-Check-Job

The CertificateType [Link-Check-Job](src/main/java/ch/puzzle/pcts/service/scheduled/CertificateTypeLinkCheckJob.java) is a scheduled job that checks the links of the certificate types.
It is configured to run every day at 03:00 AM, although this and the threshold of how many times a link can be accessed before it is considered broken can be changed in the `application.properties` file.This may also be customized for every single environment if needed.

As written in the `application.properties` file, the link check can be disabled by setting the property `app.link-check.max-retries` to 0.

### WireMock

Additionally, WireMock is also included to serve testing purposes and simulating links in the dev environment.
You may find a list of all available local links here: [Admin Mappings](http://localhost:8443/__admin/mappings)
More can be added if needed in the  [mappings](../docker/wiremock/mappings) folder.

## Api Keys

In addition to OIDC, it is also possible to authenticate via API keys. This behavior is by default disabled but can be changed via the corresponding property.
API keys need to be supplied via the `X-API-Key` header, with no additional content present in the header except for the key itself.

**Beware that all API keys have the admin role!**

### Creating new API Keys

Currently, there is no mechanism to create an API key via the GUI and, until there is demand for it, no such mechanism is planned. If you would like another mechanism for creating API keys, please create an issue.

### 1. Generate a Random Key

```bash
RAW_KEY=$(openssl rand -base64 32)
echo "Save this key — it cannot be recovered later: $RAW_KEY"
```

### 2. Hash it with BCrypt

You might need to install the bcrypt module.

```bash
HASHED=$(python3 -c "import bcrypt; print(bcrypt.hashpw(b'$RAW_KEY', bcrypt.gensalt(rounds=10)).decode())")
echo $HASHED
```

### 3. Insert it into the database

Insert it into the database, using your tool of choice.

```postgresql
INSERT INTO api_key (name, hashed_key)
VALUES ('Dev Key', '$2b$10$9UtFdznxF6ki9nxDeqJu.uQaSjHc/Z5Xfx5FGkCWm/aYYmFuCc8o.'); -- secret-key
```

### Revoking API Keys

To revoke an API key, set the `revoked` property to `true` in the database. No restart is required.
