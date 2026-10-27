
Cypress.Commands.add('loginAsUser', (user: any) => {
  cy.session(user.username, () => {
    loginWithCredentials(user.username, user.password);
  }, {
    validate() {
      cy.visit('/');
      cy.url()
        .should('not.include', Cypress.expose('LOGIN_URL'));
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

Cypress.Commands.addQuery('getByTestId', function(testId: string, text?: string) {
  const selector = `[data-testid="${testId}"]`;

  return () => {
    const elements = Cypress.$(selector);

    if (text) {
      return elements.filter((_, element) => Cypress.$(element)
        .text()
        .includes(text));
    } else {
      return elements;
    }
  };
});

Cypress.Commands.addQuery('findByTestId', function(testId: string, text?: string) {
  return (subject: JQuery<HTMLElement>) => {
    const selector = `[data-testid="${testId}"]`;
    const elements = Cypress.$(subject)
      .find(selector);

    if (text) {
      return elements.filter((_, element) => Cypress.$(element)
        .text()
        .includes(text));
    } else {
      return elements;
    }
  };
});

function loginWithCredentials(username: string, password: string) {
  cy.visit('/');

  cy.origin(Cypress.expose('LOGIN_URL'), { args: { username,
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
