import * as users from '../fixtures/users.json';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';
import OverviewPage from '../pages/overviewPage';

describe('Certificate overview', () => {
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

  describe('text search filter', () => {
    describe('text search filter', () => {
      it('should not filter when filter is empty', () => {
        const searchTerm = ' ';

        OverviewPage.fillSearchInput(searchTerm);

        cy.wait(300);

        CertificateTypeOverviewPage.table()
          .should('be.visible');
        CertificateTypeOverviewPage.tableRows()
          .should('have.length', 9);
      });

      [{ searchTerm: 'aws',
        length: 1 },
      { searchTerm: 'cer',
        length: 4 },
      {
        searchTerm: 'mic',
        length: 2
      }].forEach((table) => {
        it(`should filter when search term is ${table.searchTerm}`, () => {
          OverviewPage.fillSearchInput(table.searchTerm);

          cy.wait(300);

          CertificateTypeOverviewPage.table()
            .should('be.visible');
          CertificateTypeOverviewPage.tableRows()
            .should('have.length', table.length);
        });
      });
    });
  });
});
