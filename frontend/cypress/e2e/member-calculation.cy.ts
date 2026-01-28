import memberDetailPage from '../pages/memberDetailPage';
import MemberCalculationPage from '../pages/memberCalculationPage';
import MemberDetailPage from '../pages/memberDetailPage';
import memberCalculationPage from '../pages/memberCalculationPage';
import { memberCalculationTableData } from '../support/helper/table-data';
import { TableHelper } from '../support/helper/table-helper';

describe('MemberCalculationComponent', () => {
  let tableHelper: TableHelper;

  beforeEach(() => {
    tableHelper = TableHelper.withTableTestId('member-calculation-table');
    memberDetailPage.visit(1);
  });

  it('should contain correct member calculation table data', () => {
    memberDetailPage.visit(2);
    MemberCalculationPage.memberCalculationTab()
      .click();

    tableHelper.expectTableContains(memberCalculationTableData);
  });

  it('should disable table when no entries are found', () => {
    MemberDetailPage.visit(5);
    memberCalculationPage.memberCalculationTab()
      .click();

    tableHelper.expectEmptyTable();
  });

  it('should cut list after 10 entries', () => {
    MemberDetailPage.visit(1);
    memberCalculationPage.memberCalculationTab()
      .click();

    tableHelper.expectLengthOfTable(10)
      .expectTableToBeExtendable()
      .expandTable()
      .expectLengthOfTable(11)
      .expectTableToBeFoldable()
      .foldTable();
  });
});
