import memberDetailPage from '../pages/memberDetailPage';
import { memberCalculationTableData } from '../support/helper/table-data';
import { TableHelper } from '../support/helper/table-helper';
import * as users from '../fixtures/users.json';

describe('MemberCalculationComponent', () => {
  let tableHelper: TableHelper;

  beforeEach(() => {
    cy.loginAsUser(users.gl);

    tableHelper = TableHelper.withTableTestId('member-calculation-table');
    memberDetailPage.visit(1)
      .withTabIndex(1);
  });

  it('should contain correct member calculation table data', () => {
    memberDetailPage.visit(2)
      .withTabIndex(1);

    tableHelper.expectTableContains(memberCalculationTableData);
  });

  it('should cut list after 10 entries', () => {
    tableHelper.expectLengthOfTable(10)
      .expectTableToBeExtendable()
      .toggleShowAll()
      .expectLengthOfTable(12)
      .expectTableToBeFoldable()
      .toggleShowAll();
  });
});
