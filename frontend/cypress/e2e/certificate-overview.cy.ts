import * as users from '../fixtures/users.json';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';
import OverviewPage from '../pages/overviewPage';
import certificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';

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


  describe.only('tag filter', () => {
    it('should display options in alphabetical order', () => {
      certificateTypeOverviewPage.tagFilter();

      cy.get('mat-option')
        .should('contain', 'AWSA01');
    });

    it('should display chips in alphabetical order', () => {
      certificateTypeOverviewPage.tagFilter();

      certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');
      certificateTypeOverviewPage.selectOptionFromTagFilter('Berufsbildner');


      cy.getByTestId('chip-AWSA01')
        .should('exist');
      cy.getByTestId('chip-Berufsbildner')
        .should('exist');
    });

    it('should display the pills and select option in the dropdown when selected in dropdown', () => {
      certificateTypeOverviewPage.tagFilter();

      certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

      cy.getByTestId('chip-AWSA01')
        .should('exist');

      certificateTypeOverviewPage.tagFilter();

      certificateTypeOverviewPage.checkIfOptionFromTagFilterIsSelected('AWSA01');
    });

    it('should remove chip and deselect option in dropdown when clicked on cancel icon on the chip', () => {
      certificateTypeOverviewPage.tagFilter();
      certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

      certificateTypeOverviewPage.tagFilter();

      cy.getByTestId('chip-AWSA01')
        .should('exist');

      cy.getByTestId('chip-AWSA01-remove')
        .click();

      cy.getByTestId('chip-AWSA01')
        .should('not.exist');

      certificateTypeOverviewPage.tagFilter();
      certificateTypeOverviewPage.checkIfOptionFromTagFilterIsNotSelected('AWSA01');
    });

    it('should remove chip and deselect option in dropdown when clicked on a selected option in the dropdown', () => {
      certificateTypeOverviewPage.tagFilter();
      certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

      certificateTypeOverviewPage.checkIfOptionFromTagFilterIsSelected('AWSA01');

      certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

      certificateTypeOverviewPage.checkIfOptionFromTagFilterIsNotSelected('AWSA01');
    });
  });

  /*
   * describe('text search filter', () => {
   *   it('should not filter when filter is empty', () => {
   *     const searchTerm = ' ';
   *
   *     OverviewPage.fillSearchInput(searchTerm);
   *
   *     cy.wait(300);
   *
   *     CertificateTypeOverviewPage.table()
   *       .should('be.visible');
   *     CertificateTypeOverviewPage.tableRows()
   *       .should('have.length', 9);
   *   });
   *
   *   [{ searchTerm: 'aws',
   *     length: 1 },
   *   { searchTerm: 'cer',
   *     length: 4 },
   *   {
   *     searchTerm: 'mic',
   *     length: 2
   *   }].forEach((table) => {
   *     it(`should filter when search term is ${table.searchTerm}`, () => {
   *       OverviewPage.fillSearchInput(table.searchTerm);
   *
   *       cy.wait(300);
   *
   *       CertificateTypeOverviewPage.table()
   *         .should('be.visible');
   *       CertificateTypeOverviewPage.tableRows()
   *         .should('have.length', table.length);
   *     });
   *   });
   * });
   */
});
