import * as users from '../fixtures/users.json';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';

describe('Certificate modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    CertificateTypeOverviewPage.visit();
  });

  describe('open certificate overview', () => {
    it('should open certificate overview', () => {
      CertificateTypeOverviewPage.certificateButton();
      cy.getByTestId('generic-table')
        .should('be.visible');
    });
  });

  describe('expand and collapse certificate detail', () => {
    it('should expand and collapse the detail row', () => {
      CertificateTypeOverviewPage.certificateButton();
      CertificateTypeOverviewPage.certificateRows();

      CertificateTypeOverviewPage.certificateDetailView()
        .should('be.visible');

      CertificateTypeOverviewPage.certificateRows();
      CertificateTypeOverviewPage.certificateDetailView()
        .should('not.be.visible');
    });
  });

  describe('open example link in a new page', () => {
    it.only('should have correct certificate detail url set', () => {
      cy.visit('/certificate');

      CertificateTypeOverviewPage.certificateButton();
      CertificateTypeOverviewPage.certificateRows();

      cy.getByTestId('certificate-overview')
        .shadow()
        .findByTestId('certificate-overview')
        .invoke('attr', 'href')
        .should('eq', '/certificate');
    });
  });
});
