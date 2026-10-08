import * as users from '../fixtures/users.json';
import CertificateTypeOverviewPage from '../pages/certificateTypeOverviewPage';
import OverviewPage from '../pages/overviewPage';

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

  describe.only('text search filter', () => {
    it('should not filter when search term is whitespace', () => {
      OverviewPage.fillSearchInput(' ');

      CertificateTypeOverviewPage.tableRows()
        .should('have.length', 3);

      cy.getByTestId('limit-list-button')
        .should('be.visible');
    });

    const filterScenarios = [{ searchTerm: 'aws',
      expectedLength: 1,
      expectLimitButton: false },
    { searchTerm: 'cer',
      expectedLength: 4,
      expectLimitButton: true },
    { searchTerm: 'mic',
      expectedLength: 2,
      expectLimitButton: false }];

    filterScenarios.forEach(({ searchTerm, expectedLength, expectLimitButton }) => {
      it(`should filter correctly when search term is "${searchTerm}"`, () => {
        cy.getByTestId('limit-list-button')
          .click();

        OverviewPage.fillSearchInput(searchTerm);

        CertificateTypeOverviewPage.tableRows()
          .should('have.length', expectedLength);

        if (expectLimitButton) {
          cy.getByTestId('limit-list-button')
            .should('be.visible');
        } else {
          cy.getByTestId('limit-list-button')
            .should('not.exist');
        }
      });
    });

    it('should filter and slice when limit is reached', () => {
      cy.getByTestId('limit-list-button')
        .click();
      OverviewPage.fillSearchInput('cer');

      CertificateTypeOverviewPage.tableRows()
        .should('have.length', 4);
      cy.getByTestId('limit-list-button')
        .should('be.visible');

      cy.getByTestId('limit-list-button')
        .click();
      CertificateTypeOverviewPage.tableRows()
        .should('have.length', 3);

      cy.getByTestId('limit-list-button')
        .click();
      CertificateTypeOverviewPage.tableRows()
        .should('have.length', 4);
    });
  });

  /*
   * describe.only('text search filter', () => {
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
