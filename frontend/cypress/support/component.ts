import './commands';

declare global {
  export namespace Cypress {
    interface Chainable {
      visitDefaultPage(): void;
      getByTestId(testsId: string, text?: string): Chainable;
      findByTestId(testId: string, text?: string): Chainable;
    }
  }
}
