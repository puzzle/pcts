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

## 📖 README: Scoped Translation System

### Overview

The Scoped Translation system provides a powerful way to manage i18n keys in reusable components. It allows you to define a "prefix" for a specific route or component (e.g., `MEMBER.FORM.ADD`). Inside that component's template, you can use short, relative keys (e.g., `'TITLE'`).

The system automatically combines the prefix and the key (`MEMBER.FORM.ADD.TITLE`) and even provides a sophisticated fallback mechanism, allowing for highly reusable components and cleaner translation files.

### How to Use

There are two main steps:

1. **Provide the Prefix:** Define the scope in your component or (more commonly) your route configuration.
1. **Use the Pipe:** Use the `scopedTranslation` pipe in your component's template.

---

#### 1. Providing the i18n Prefix

Use the `provideI18nPrefix` helper function in the `providers` array of your component or route definition. This is most powerful when used in routing.

##### Example: `routes.ts`

This setup uses the _same_ `MemberFormComponent` for both "add" and "edit" operations, but provides a different translation prefix for each.

```typescript
// ... imports
import { provideI18nPrefix } from './path/to/provideI18nPrefix'

export const routes: Routes = [
  {
    path: '',
    children: [
      // ... other routes
      {
        path: 'add',
        component: MemberFormComponent,
        providers: [
          // This component will now use 'MEMBER.FORM.ADD' as its prefix
          provideI18nPrefix('MEMBER.FORM.ADD'),
        ],
      },
      {
        path: ':id/edit',
        component: MemberFormComponent,
        resolve: { member: memberDataResolver },
        providers: [
          // The *same* component will use 'MEMBER.FORM.EDIT' here
          provideI18nPrefix('MEMBER.FORM.EDIT'),
        ],
      },
    ],
  },
]
```

The `provideI18nPrefix` function also supports nesting. If a parent route provided `MEMBER` and a child route provided `FORM`, the resulting prefix would be `MEMBER.FORM`.

---

#### 2. Using the `scopedTranslation` Pipe

In the component that has a prefix provided (like `MemberFormComponent`), you can now use the `scopedTranslation` pipe instead of the standard `translate` pipe.

##### Example: `member-form.component.html`

```html
<h1>{{ 'TITLE' | scopedTranslation }}</h1>

<form ...>
  <button type="button">{{ 'BUTTONS.CANCEL' | scopedTranslation }}</button>
  <button type="submit">{{ 'BUTTONS.ACTION' | scopedTranslation }}</button>
</form>
```

---

### The Fallback Hierarchy (Key Feature)

The true power of this system is its automatic fallback. When you request a key, the service searches for the most specific translation first, then works its way up to the most generic.

If your component has the prefix `MEMBER.FORM.ADD` and you request the key `TITLE`, the service will search for a valid translation in this exact order:

1. `MEMBER.FORM.ADD.TITLE` 1.`MEMBER.FORM.TITLE`
1. `MEMBER.TITLE`
1. `TITLE`

This allows you to define common translations at a high level and override them only when necessary.

---

### Putting It All Together: Full Example

1. **Translation File (`en.json`)**

   Notice how we can define common keys at the `MEMBER.FORM` level and specific overrides inside `ADD` and `EDIT`.

   ```json
   {
     "MEMBER": {
       "FORM": {
         "TITLE": "Default Member Form", // Generic fallback
         "BUTTONS": {
           "CANCEL": "Cancel" // Shared by all forms
         },
         "ADD": {
           "TITLE": "Create New Member", // Specific override
           "BUTTONS": {
             "ACTION": "Create" // Specific override
           }
         },
         "EDIT": {
           "TITLE": "Edit Member", // Specific override
           "BUTTONS": {
             "ACTION": "Save Changes" // Specific override
           }
         }
       }
     }
   }
   ```

1. **Routing (`routes.ts`)**

   _(See "Providing the i18n Prefix" section above)_

1. **Template (`member-form.component.html`)**

   ```html
   <h1>{{ 'TITLE' | scopedTranslation }}</h1>

   <button>{{ 'BUTTONS.CANCEL' | scopedTranslation }}</button>
   <button>{{ 'BUTTONS.ACTION' | scopedTranslation }}</button>
   ```

#### Result

- **When a user navigates to `/members/add`:**
  - `'TITLE'` resolves to `MEMBER.FORM.ADD.TITLE` -> "Create New Member"
  - `'BUTTONS.CANCEL'` resolves to `MEMBER.FORM.BUTTONS.CANCEL` -> "Cancel"
  - `'BUTTONS.ACTION'` resolves to `MEMBER.FORM.ADD.BUTTONS.ACTION` -> "Create"

- **When a user navigates to `/members/1/edit`:**
  - `'TITLE'` resolves to `MEMBER.FORM.EDIT.TITLE` -> "Edit Member"
  - `'BUTTONS.CANCEL'` resolves to `MEMBER.FORM.BUTTONS.CANCEL` -> "Cancel"
  - `'BUTTONS.ACTION'` resolves to `MEMBER.FORM.EDIT.BUTTONS.ACTION` -> "Save Changes"
