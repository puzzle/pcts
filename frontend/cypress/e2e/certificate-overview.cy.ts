import * as users from '../fixtures/users.json';
import memberDetailPage from '../pages/memberDetailPage';

describe('Certificate modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  describe('open certificate overview', () => {
    it('should open certificate overview', () => {
      cy.getByTestId('certificate-overview')
        .click();
      cy.getByTestId('generic-table')
        .should('be.visible');
    });
  });

  describe('expand and collapse certificate detail', () => {
    it('should expand and collapse the detail row', () => {
      cy.getByTestId('certificate-overview')
        .click();

      cy.getByTestId('expand-row')
        .first()
        .click();
      cy.get('.detail-content')
        .first()
        .should('be.visible');

      cy.getByTestId('expand-row')
        .first()
        .click();

      cy.get('.detail-content')
        .first()
        .should('not.be.visible');
    });
  });

  describe('open example link in a new page', () => {
    it('should open the certificate detail link', () => {
      cy.getByTestId('certificate-overview')
        .click();

      cy.getByTestId('expand-row')
        .first()
        .click();

      cy.get('.detail-content')
        .first()
        .find('a')
        .click();
    });
  });
});


