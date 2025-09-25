import HomePage from '../pages/homePage';
import translations from '../../public/i18n/de.json';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    HomePage.visitDefaultPage();
  });

  it('should display the page title and member count', () => {
    cy.get('h1')
      .should('contain.text', translations.MEMBER.OVERVIEW.MEMBERS)
      .and('contain.text', '(');

    cy.get('table')
      .should('exist');
  });

  it('should search members by name', () => {
    cy.get('input[matinput]')
      .type('Ja Morant');

    cy.get('tr.mat-mdc-row')
      .each(($row) => {
        cy.wrap($row)
          .invoke('text')
          .then((text) => {
            expect(text.toLowerCase()).to.include('ja morant');
          });
      });
  });

  it('should show "no results" message if nothing matches', () => {
    cy.get('input[matinput]')
      .clear()
      .type('zzzzzzzzz');
    cy.get('tr.mat-mdc-no-data-row')
      .should('exist');
    cy.get('tr.mat-mdc-no-data-row')
      .should('contain.text', translations.MEMBER.OVERVIEW.NO_SEARCH_RESULT);
  });


  it('should filter members by employment state', () => {
    cy.get('button.mdc-button')
      .eq(1)
      .click();

    cy.get('table tr.mat-mdc-row')
      .each(($row) => {
        cy.wrap($row)
          .find('td')
          .last()
          .invoke('text')
          .then((statusText) => {
            // eslint-disable-next-line
            expect(statusText.trim()).not.to.be.empty;
          });
      });
  });

  it('should reset filters when "All" is clicked', () => {
    cy.get('button.mdc-button')
      .contains(translations.MEMBER.OVERVIEW.ALL)
      .click();
    cy.get('tr.mat-mdc-row')
      .should('exist');
  });

  it('should keep query params in the URL when filtering', () => {
    cy.get('input[matinput]')
      .clear()
      .type('Jane');
    cy.url()
      .should('include', 'q=Jane');
  });

  it('should keep query params in the URL when filtering', () => {
    cy.get('button.mdc-button')
      .contains(translations.MEMBER.EMPLOYMENT_STATUS_VALUES.APPLICANT)
      .click();
    cy.url()
      .should('include', 'status=APPLICANT');
  });
});
