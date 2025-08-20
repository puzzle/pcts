# PCTS Backend
The backend of this project is written in Spring Boot.

## Formatting
We use the ***spotless*** Plugin for formatting the Java code:
https://github.com/diffplug/spotless

### Checking and applying formatting
- To check the code formatting run `mvn spotless:check`
- To then format the code run `mvn spotless:apply`

If you have not set up our [Git Hooks](../README.md#git-hooks) we strongly encourage you to do so since they also contain a check for the formatting whenever you commit.

### How to update the spotless configuration using Intellij IDEA:
- Open `Go to Settings -> Editor -> Code styles -> Java`
- Select the default project config and export it to a file
- Then make the changes you want to the code style config. Make sure to ***HIT APPLY*** and then export it as a file
- Afterward run the following command `git --no-pager diff --no-index -U0 default.xml changed.xml | egrep '^\+' | diff-so-fancy` to see the changes
- Finally copy all additions to the formatter file in the backend project

## Api Documentation
We use ***springdoc-openapi*** library for documenting the api.

### Accessing the Api Documentation

Our API documentation is generated using ***springdoc-openapi***. You can access it through a web interface (Swagger UI) or as a raw json file (OpenAPI 3 specification).

#### Access Points

**For Development**
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [OpenAPI 3 specification](http://localhost:8080/v3/api-docs)