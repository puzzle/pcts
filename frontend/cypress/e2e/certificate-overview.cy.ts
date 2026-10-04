import * as users from '../fixtures/users.json';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';

describe('Certificate modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    CertificateTypeOverviewPage.visit();
  });

  describe('open certificate overview', () => {
    it('should open certificate overview', () => {
      cy.visit('/');

      CertificateTypeOverviewPage.visitViaButton();

      CertificateTypeOverviewPage.table()
        .should('be.visible');
    });
  });

  describe('expand and collapse certificate detail', () => {
    it('should expand and collapse the detail row', () => {
      CertificateTypeOverviewPage.firstCertificateRow()
        .click();

      CertificateTypeOverviewPage.certificateDetailView()
        .should('be.visible');

      CertificateTypeOverviewPage.firstCertificateRow()
        .click();
      CertificateTypeOverviewPage.certificateDetailView()
        .should('not.be.visible');
    });
  });

  it('should have a valid link and open it in a new tab', () => {
    CertificateTypeOverviewPage.firstCertificateRow()
      .click();

    CertificateTypeOverviewPage.certificateDetailView()
      .find('a')
      .should('have.attr', 'href')
      .and('not.be.empty');

    CertificateTypeOverviewPage.certificateDetailView()
      .find('a')
      .should('have.attr', 'target', '_blank');
  });
});
