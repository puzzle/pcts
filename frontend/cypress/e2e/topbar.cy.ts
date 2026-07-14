import * as users from '../fixtures/users.json';

describe('Topbar', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should have correct url set', () => {
    cy.intercept('GET', 'http://localhost:4200/api/v1/configuration/app')
      .as('getURL');

    cy.visit('/');

    cy.wait('@getURL');

    cy.getByTestId('support-page-url')
      .shadow()
      .findByTestId('support-page-url')
      .invoke('attr', 'href')
      .then((href: string) => {
        cy.get('@getURL.1')
          .its('response.body.helpUrl')
          .should('eq', href);
      });
  });
});
