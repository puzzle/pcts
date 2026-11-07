# CertificateType Link-Check-Job

The CertificateType [Link-Check-Job](src/main/java/ch/puzzle/pcts/service/scheduled/CertificateTypeLinkCheckJob.java) is a scheduled job that checks the links of the certificate types.
It is configured to run every day at 03:00 AM, although this and the threshold of how many times a link can be accessed before it is considered broken can be changed in the `application.properties` file.This may also be customized for every single environment if needed.

As written in the `application.properties` file, the link check can be disabled by setting the property `app.link-check.max-retries` to 0.

## WireMock

Additionally, WireMock is also included to serve testing purposes and simulating links in the dev environment.
You may find a list of all available local links here: [Admin Mappings](http://localhost:8443/__admin/mappings).

More can be added if needed in the  [mappings](../docker/wiremock/mappings) folder.
