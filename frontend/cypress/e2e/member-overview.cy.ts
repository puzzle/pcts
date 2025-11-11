import translations from '../../public/i18n/de.json';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';
import FormPage from '../pages/formPage';
import OverviewPage from '../pages/overviewPage';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    OverviewPage.visit();
  });

  it('should search members by name', () => {
    OverviewPage.fillSearchInput('Lena');
    cy.wait(300);
    OverviewPage.memberRows()
      .contains('Lena Müller', { matchCase: false });
  });

  it('should display the page title and member count', () => {
    OverviewPage.title()
      .should('contain.text', translations.MEMBER.MODEL_NAME_PLURAL)
      .and('contain.text', '(');

    OverviewPage.memberTable()
      .should('exist');
  });

  it('should show "no results" message if nothing matches', () => {
    const searchInput = 'zzzzzzzzz';
    OverviewPage.fillSearchInput(searchInput);
    OverviewPage.noResultsRow()
      .should('exist');
    const expectedText = translations.TABLE.NO_SEARCH_RESULT
      .replace('{{searchValue}}', searchInput)
      .replace('{{model}}', 'Member');
    OverviewPage.noResultsRow()
      .should('contain.text', expectedText);
  });

  it('should filter members by employment state', () => {
    OverviewPage.filterByState(EmploymentState.APPLICANT)
      .click();

    OverviewPage.memberRows()
      .each(($row) => {
        cy.wrap($row)
          .findByTestId('member-status')
          .should('include.text', translations.MEMBER.EMPLOYMENT_STATUS_VALUES.APPLICANT);
      });
  });

  it('should reset filters when "All" is clicked', () => {
    OverviewPage.resetEmploymentStateFilter()
      .click();
    OverviewPage.memberRows()
      .should('exist');
  });

  it('should keep query params in the URL when searching', () => {
    OverviewPage.fillSearchInput('Jane');
    cy.url()
      .should('include', 'q=Jane');
  });

  it('should load add member page', () => {
    OverviewPage.createMemberButton()
      .click();

    FormPage.shouldHaveTitle(translations.FORM.ADD.ACTION, 'Member');
  });

  it('should keep query params in the URL when filtering', () => {
    OverviewPage.filterByState(EmploymentState.APPLICANT)
      .click();
    cy.url()
      .should('include', 'status=APPLICANT');
  });

  const sortTests: [string, string, string][] = [
    ['employment-state',
      'Bewerber',
      'Member'],
    ['last-name',
      'Becker',
      'Weber'],
    ['birth-date',
      '03.03.1998',
      '05.08.2001'],
    ['organisation-unit-name',
      '/bbt',
      '/zh']
  ];

  sortTests.forEach(([sortAttribute,
    ascParam,
    descParam]) => {
    it(`should test sorting on ${sortAttribute}`, () => {
      // Is set as default so no need to sort
      if (sortAttribute != 'last-name') {
        OverviewPage.sortAttributeAsc(sortAttribute);
      }
      OverviewPage.firstMemberRows()
        .should('contain.text', ascParam);

      OverviewPage.sortAttributeDesc(sortAttribute);
      OverviewPage.firstMemberRows()
        .should('contain.text', descParam);
    });
  });
});
