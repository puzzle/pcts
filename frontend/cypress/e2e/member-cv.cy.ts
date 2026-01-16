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
});
