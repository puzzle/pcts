import Chainable = Cypress.Chainable;

Cypress.Commands.add('getByTestId', (testId: string, text?: string): Chainable => {
  const selector = `[data-testid="${testId}"]`;

  if (text) {
    return cy.get(selector)
      .contains(text);
  } else {
    return cy.get(selector);
  }
});

Cypress.Commands.add('findByTestId', { prevSubject: true }, (subject: JQuery<HTMLElement>, testId: string, text?: string): Chainable => {
  const selector = `[data-testid="${testId}"]`;
  if (text) {
    return cy.wrap(subject)
      .find(selector)
      .contains(text);
  } else {
    return cy.wrap(subject)
      .find(selector);
  }
});
