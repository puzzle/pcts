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


  describe('tag filter', () => {
    describe('tag selecting and deselecting', () => {
      beforeEach(() => {
        certificateTypeOverviewPage.tagFilter();
      });

      it('should display options in alphabetical order', () => {
        const options: string[] = [];

        cy.get('mat-option')
          .each(($option) => {
            options.push($option.text()
              .trim());
          })
          .then(() => {
            const sortedOptions = [...options].sort((a, b) => a.localeCompare(b));

            expect(sortedOptions).to.deep.equal(options);
          });
      });

      it('should display chips in alphabetical order', () => {
        const options: string[] = [];

        cy.get('mat-option')
          .each(($option) => {
            const optionTitle = $option.text()
              .trim();

            certificateTypeOverviewPage.selectOptionFromTagFilter(optionTitle);
            options.push(optionTitle);
          });

        const chips: string[] = [];

        certificateTypeOverviewPage.tagFilter();

        cy.get('mat-chip-row')
          .each(($chip) => {
            const chipTitle = $chip.children('span.mat-mdc-chip-action')
              .text()
              .trim();
            chips.push(chipTitle);
          })
          .then(() => {
            expect(chips).to.deep.equal(options);
          });
      });

      it('should display the pills and select option in the dropdown when selected in dropdown', () => {
        certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

        cy.getByTestId('chip-AWSA01')
          .should('exist');

        certificateTypeOverviewPage.checkIfOptionFromTagFilterIsSelected('AWSA01');

        cy.url()
          .should('include', 'tag=AWSA01');
      });

      it('should remove chip and deselect option in dropdown when clicked on cancel icon on the chip', () => {
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
        cy.url()
          .should('not.include', 'tag=AWSA01');
      });

      it('should remove chip and deselect option in dropdown when clicked on a selected option in the dropdown', () => {
        certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

        certificateTypeOverviewPage.checkIfOptionFromTagFilterIsSelected('AWSA01');
        cy.getByTestId('chip-AWSA01')
          .should('exist');

        certificateTypeOverviewPage.selectOptionFromTagFilter('AWSA01');

        certificateTypeOverviewPage.checkIfOptionFromTagFilterIsNotSelected('AWSA01');
        cy.getByTestId('chip-AWSA01')
          .should('not.exist');
        cy.url()
          .should('not.include', 'tag=AWSA01');
      });
    });

    it('should not filter when no tag is selected', () => {
      CertificateTypeOverviewPage.table()
        .should('be.visible');
      CertificateTypeOverviewPage.tableRows()
        .should('have.length', 9);
    });
    [
      { tags: ['AWSA01'],
        length: 1,
        searchTerm: '' },
      { tags: ['Berufsbildner'],
        length: 2,
        searchTerm: '' },
      { tags: ['GitLab 101'],
        length: 1,
        searchTerm: '' },
      { tags: ['Berufsbildner'],
        length: 1,
        searchTerm: 'Red' },
      { tags: ['AWSA01',
        'Machine learning'],
      length: 2,
      searchTerm: '' }
    ].forEach((table) => {
      const searchTermTitle = table.searchTerm ? table.searchTerm : 'empty';
      const tagTitle = table.tags.join(', ');

      it(`should filter when searchTerm is ${searchTermTitle} and with ${tagTitle} tag`, () => {
        if (table.searchTerm) {
          OverviewPage.fillSearchInput(table.searchTerm);

          cy.wait(300);
        }

        certificateTypeOverviewPage.tagFilter();

        table.tags.forEach((tag) => certificateTypeOverviewPage.selectOptionFromTagFilter(tag));

        CertificateTypeOverviewPage.table()
          .should('be.visible');
        CertificateTypeOverviewPage.tableRows()
          .should('have.length', table.length);
      });
    });
  });
});
