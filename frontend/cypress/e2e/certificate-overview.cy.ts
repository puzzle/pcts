import * as users from '../fixtures/users.json';
import memberDetailPage from '../pages/memberDetailPage';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';

describe('Certificate modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
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
    it('should open the certificate detail link', () => {
      CertificateTypeOverviewPage.certificateButton();
      CertificateTypeOverviewPage.certificateRows();

      CertificateTypeOverviewPage.certificateDetailView()
        .find('a')
        .click();
    });
  });
});


