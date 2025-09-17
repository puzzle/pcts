import './commands';

declare global {
  export namespace Cypress {
    interface Chainable {
      visitDefaultPage(): void;
    }
  }
}
