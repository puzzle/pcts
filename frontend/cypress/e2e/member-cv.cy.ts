import memberDetailPage from '../pages/memberDetailPage';
import CvMemberPage from '../pages/cvMemberPage';
import {
  certificateTableData,
  degreeTableData,
  experienceTableData,
  leadershipExperienceTableData
} from '../support/helper/table-data';
import MemberDetailPage from '../pages/memberDetailPage';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    // Equals to 'Lena Müller'
    memberDetailPage.visit(1);
  });

  const tableData: [string, string[]][] = [
    ['degree',
      degreeTableData],
    ['experience',
      experienceTableData],
    ['certificate',
      certificateTableData],
    ['leadership-experience',
      leadershipExperienceTableData]
  ];

  tableData.forEach(([tableName,
    tableDataList]) => {
    it(`should contain correct ${tableName} table data`, () => {
      MemberDetailPage.visit(2);
      CvMemberPage.cvTable(tableName);
      tableDataList.forEach((tableString) => {
        CvMemberPage.cvTable(tableName)
          .should('include.text', tableString);
      });
    });
  });

  it('should disable table when no entries are found', () => {
    MemberDetailPage.visit(1);
    CvMemberPage.cvTable('leadership-experience')
      .should('include.text', 'Keine Einträge');
  });

  it('should cut list after 5 entries', () => {
    MemberDetailPage.visit(1);
    CvMemberPage.cvTable('experience');
    CvMemberPage.cvTableRows('experience')
      .should('have.length', 10);
    CvMemberPage.showListButton()
      .should('include.text', 'Alles anzeigen')
      .click();
    CvMemberPage.cvTableRows('experience')
      .should('have.length', 13);
    CvMemberPage.showListButton()
      .should('include.text', 'Weniger anzeigen');
  });

  it('should include position text even if employer is not set', () => {
    MemberDetailPage.visit(3);
    CvMemberPage.cvTable('experience')
      .should('include.text', 'Data Analyst');
  });
});
