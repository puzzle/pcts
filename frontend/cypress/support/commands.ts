import Chainable = Cypress.Chainable;

Cypress.Commands.add('loginAsUser', (user: any) => {
  cy.session(user.username, () => {
    loginWithCredentials(user.username, user.password);
  }, {
    validate() {
      cy.visit('/');
      cy.url()
        .should('not.include', Cypress.env('LOGIN_URL'));
    }
  });
});

Cypress.Commands.add('logout', () => {
  cy.title()
    .should('equal', 'PCTS');
  cy.getByTestId('user-options')
    .as('userOptions');
  cy.get('@userOptions')
    .click();

  cy.getByTestId('logout')
    .as('logoutButton');
  cy.get('@logoutButton')
    .click();
});

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


function loginWithCredentials(username: string, password: string) {
  cy.visit('/');

  cy.origin(Cypress.env('LOGIN_URL'), { args: { username,
    password } }, ({ username, password }) => {
    cy.get('input[name="username"]')
      .type(username);
    cy.get('input[name="password"]')
      .type(password);
    cy.get('button[type="submit"]')
      .click();
  });

  cy.url()
    .should('include', Cypress.config().baseUrl);
}
