import * as users from '../fixtures/users.json';
import translations from '../../public/i18n/de.json';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';
import FormPage from '../pages/formPage';
import OverviewPage from '../pages/overviewPage';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    OverviewPage.visit();
  });

  it('should search members by name', () => {
    OverviewPage.fillSearchInput('Lena');
    cy.wait(300);
    OverviewPage.memberRows()
      .contains('Lena Müller', { matchCase: false });
  });

  it('should display the page title and member count', () => {
    OverviewPage.memberRows()
      .its('length')
      .then((count) => {
        OverviewPage.title()
          .should('contain.text', translations.MEMBER.MODEL_NAME_PLURAL)
          .and('contain.text', '(' + count + ')');
      });

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
    OverviewPage.title()
      .should('contain.text', '(0)');
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
    OverviewPage.memberRows()
      .its('length')
      .then((count) => {
        OverviewPage.title()
          .should('contain.text', '(' + count + ')');
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

  it('should visit detail page of member', () => {
    OverviewPage.getMemberByFullName('Lena', 'Müller')
      .click();

    cy.url()
      .should('include', '/member/1');
  });

  const sortTests: [string, string, string][] = [
    ['employment-state',
      'Bewerber',
      'Member'],
    ['first-name',
      'Abraham',
      'Tobias'],
    ['last-name',
      'Woodard',
      'Weber'],
    ['birth-date',
      '03.03.1998',
      '29.02.2004'],
    ['organisation-unit-name',
      '-',
      '/zh']
  ];

  sortTests.forEach(([sortAttribute,
    expectedAsc,
    expectedDesc]) => {
    it(`should test sorting on ${sortAttribute}`, () => {
      // Is set as default so no need to sort
      if (sortAttribute != 'last-name') {
        OverviewPage.sortAttribute(sortAttribute);
      }
      OverviewPage.firstMemberRows()
        .should('contain.text', expectedAsc);

      OverviewPage.sortAttribute(sortAttribute);
      OverviewPage.firstMemberRows()
        .should('contain.text', expectedDesc);
    });
  });
});
