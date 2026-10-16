Cypress.Commands.add('loginAsUser', (user: any) => {
  cy.session(user.username, () => {
    loginWithCredentials(user.username, user.password);
  }, {
    validate() {
      cy.visitApp('/');
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

Cypress.Commands.add('visitApp', (url: string) => {
  /*
   * keycloak (login-required) redirects after the load event cy.visit waits for,
   * without waiting for the app the next visit races that redirect
   */
  cy.intercept('GET', '**/api/v1/members/myself')
    .as('myself');
  cy.visit(url);
  cy.wait('@myself');
});

Cypress.Commands.addQuery('getByTestId', (testId: string, text?: string) => {
  const getFn = cy.now('get', `[data-testid="${testId}"]`) as (subject?: unknown) => JQuery;
  const containsFn = text ? cy.now('contains', text) as (subject: JQuery) => JQuery : undefined;

  return (subject) => {
    const $elements = getFn(subject);
    return containsFn ? containsFn($elements) : $elements;
  };
});

Cypress.Commands.addQuery('findByTestId', (testId: string, text?: string) => {
  const findFn = cy.now('find', `[data-testid="${testId}"]`) as (subject: JQuery) => JQuery;
  const containsFn = text ? cy.now('contains', text) as (subject: JQuery) => JQuery : undefined;

  return (subject: JQuery) => {
    const $elements = findFn(subject);
    return containsFn ? containsFn($elements) : $elements;
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
