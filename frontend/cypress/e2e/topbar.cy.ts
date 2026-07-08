import * as users from '../fixtures/users.json';

describe('Topbar', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  it('should have correct url on help', () => {
    cy.getByTestId('support-page-url')
      .should('have.attr', 'href')
      .then((href) => {
        cy.visit(href);
      });
    cy.url()
      .should('eq', 'https://google.com');
  });
});
