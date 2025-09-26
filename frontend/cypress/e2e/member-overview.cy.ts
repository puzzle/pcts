import HomePage from '../pages/homePage';
import translations from '../../public/i18n/de.json';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    HomePage.visit();
  });

  it('should search members by name', () => {
    HomePage.fillSearchInput('Ja');
    cy.wait(300);
    HomePage.memberRows()
      .contains('Ja Morant', { matchCase: false });
  });

  it('should display the page title and member count', () => {
    HomePage.title()
      .should('contain.text', translations.MEMBER.OVERVIEW.MEMBERS)
      .and('contain.text', '(');

    HomePage.memberTable()
      .should('exist');
  });

  it('should show "no results" message if nothing matches', () => {
    const searchInput = 'zzzzzzzzz';
    HomePage.fillSearchInput(searchInput);
    HomePage.noResultsRow()
      .should('exist');
    const expectedText = translations.MEMBER.OVERVIEW.NO_SEARCH_RESULT.replace('{{searchValue}}', searchInput);
    HomePage.noResultsRow()
      .should('contain.text', expectedText);
  });

  it('should filter members by employment state', () => {
    HomePage.filterByState(EmploymentState.APPLICANT)
      .click();

    HomePage.memberRows()
      .each(($row) => {
        cy.wrap($row)
          .findByTestId('member-status')
          .should('include.text', translations.MEMBER.EMPLOYMENT_STATUS_VALUES.APPLICANT);
      });
  });

  it('should reset filters when "All" is clicked', () => {
    HomePage.filterAll()
      .click();
    HomePage.memberRows()
      .should('exist');
  });

  it('should keep query params in the URL when searching', () => {
    HomePage.fillSearchInput('Jane');
    cy.url()
      .should('include', 'q=Jane');
  });

  it('should keep query params in the URL when filtering', () => {
    HomePage.filterByState(EmploymentState.APPLICANT)
      .click();
    cy.url()
      .should('include', 'status=APPLICANT');
  });
});
