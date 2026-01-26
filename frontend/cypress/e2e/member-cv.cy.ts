import { certificateTableData, degreeTableData, experienceTableData, leadershipExperienceTableData, TableData } from '../support/helper/table-data';
import memberDetailPage from '../pages/memberDetailPage';
import MemberDetailPage from '../pages/memberDetailPage';
import * as users from '../fixtures/users.json';
import { TableHelper } from '../support/helper/table-helper';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    // Equals to 'Lena Müller'
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  const tables: [string, TableData][] = [
    ['degree',
      degreeTableData],
    ['experience',
      experienceTableData],
    ['certificate',
      certificateTableData],
    ['leadership-experience',
      leadershipExperienceTableData]
  ];

  tables.forEach(([name,
    content]) => {
    it(`should contain correct ${name} table data`, () => {
      MemberDetailPage.visit(2);
      TableHelper.withTableTestId(`cv-table-${name}`)
        .expectTableContains(content);
    });
  });

  it('should disable table when no entries are found', () => {
    TableHelper.withTableTestId('cv-table-leadership-experience')
      .expectEmptyTable();
  });

  it('should cut list after 5 entries', () => {
    TableHelper.withTableTestId('cv-table-experience')
      .expectLengthOfTable(10)
      .expectTableToBeExtendable()
      .toggleShowAll()
      .expectLengthOfTable(13)
      .expectTableToBeFoldable()
      .toggleShowAll();
  });

  it('should include position text even if employer is not set', () => {
    MemberDetailPage.visit(3);
    TableHelper.withTableTestId('cv-table-experience')
      .getTableHTMLElement()
      .should('include.text', 'Data Analyst');
  });
});
