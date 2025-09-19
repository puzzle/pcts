# Pcts

This project was generated using [Angular CLI](https://github.com/angular/angular-cli)
version 20.0.0.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`.
The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools.
To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics
(such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory.
By default, the production build optimizes your application for performance and speed.

## Running tests

We use [Jest](https://jestjs.io/) for frontend testing.

```bash
npm run test
```

## End-to-end tests

We use [Cypress](https://www.cypress.io/) for running our end-to-end tests. Here is everything you need to know:

### Local setup

To run the backend for Cypress testing, you need to start the application with the correct Docker profile. To achieve this, make sure you're in the docker directory and execute this command: `./e2e-application-start`

### Running Tests

**To run all tests headless use:**

- `npm run cypress:run`

**To only run selected tests headfull use:**

- `npm run cypress:open`

### In case of failing tests

- Make sure you do not have any additional or missing data in your database from a previous testrun
- Confirm that the application is healthy and running
- Restart the application and / or restart cypress
- Otherwise, check the logs

## Running formatters

```bash
# Check code formatting:
npm run check-linting:all
```

```bash
# Format the code:
npm run format:all
```

Angular CLI does not come with an end-to-end testing framework by default.
You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed
command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli)
page.
