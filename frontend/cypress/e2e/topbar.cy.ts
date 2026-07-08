import * as users from '../fixtures/users.json';

describe('Topbar', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    cy.visit('/');
  });

  it('should have correct url on help', () => {
    cy.getByTestId('support-page-url')
      .should('have.attr', 'href', 'https://dummy-url.test');
  });
});
