import { certificateTableData, degreeTableData, experienceTableData, leadershipExperienceTableData, TableData } from '../support/helper/table-data';
import memberDetailPage from '../pages/memberDetailPage';
import * as users from '../fixtures/users.json';
import { TableHelper } from '../support/helper/table-helper';

describe('MemberOverviewComponent', () => {
  describe('member 2', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      memberDetailPage.visit(2);
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
        TableHelper.withTableTestId(`cv-table-${name}`)
          .expectTableContains(content);
      });
    });
  });

  describe('member 1', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      memberDetailPage.visit(1);
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
  });

  describe('member 3', () => {
    beforeEach(() => {
      cy.loginAsUser(users.gl);
      memberDetailPage.visit(3);
    });

    it('should include position text even if employer is not set', () => {
      TableHelper.withTableTestId('cv-table-experience')
        .getTableHTMLElement()
        .should('include.text', 'Data Analyst');
    });
  });
});
