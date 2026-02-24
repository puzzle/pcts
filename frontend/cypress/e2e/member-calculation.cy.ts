import memberDetailPage from '../pages/memberDetailPage';
import { memberCalculationTableData } from '../support/helper/table-data';
import { TableHelper } from '../support/helper/table-helper';

describe('MemberCalculationComponent', () => {
  let tableHelper: TableHelper;

  beforeEach(() => {
    tableHelper = TableHelper.withTableTestId('member-calculation-table');
  });

  it('should contain correct member calculation table data', () => {
    memberDetailPage.visit(2)
      .switchTab('Manager');

    tableHelper.expectTableContains(memberCalculationTableData);
  });

  it('should cut list after 10 entries', () => {
    memberDetailPage.visit(1)
      .switchTab('Administrator');

    tableHelper.expectLengthOfTable(10)
      .expectTableToBeExtendable()
      .toggleShowAll()
      .expectLengthOfTable(12)
      .expectTableToBeFoldable()
      .toggleShowAll();
  });
});
