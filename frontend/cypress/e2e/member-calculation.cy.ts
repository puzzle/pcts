import memberDetailPage from '../pages/memberDetailPage';
import { memberCalculationTableData } from '../support/helper/table-data';
import { TableHelper } from '../support/helper/table-helper';
import * as users from '../fixtures/users.json';
import calculationDetailViewPage from '../pages/calculationDetailViewPage';
import { Relevancy } from '../../src/app/features/calculations/relevancy.enum';


describe('MemberCalculationComponent', () => {
  let calculationTableHelper: TableHelper;

  let experienceTableHelper: TableHelper;
  let degreeTableHelper: TableHelper;
  let certificateTableHelper: TableHelper;
  let leadershipExperienceTableHelper: TableHelper;

  beforeEach(() => {
    cy.loginAsUser(users.gl);
    calculationTableHelper = TableHelper.withTableTestId('member-calculation-table');
    experienceTableHelper = TableHelper.withTableTestId('experience-table');
    degreeTableHelper = TableHelper.withTableTestId('degree-table');
    certificateTableHelper = TableHelper.withTableTestId('certificate-table');
    leadershipExperienceTableHelper = TableHelper.withTableTestId('leadership-experience-table');
  });

  it('should contain correct member calculation table data', () => {
    memberDetailPage.visit(2)
      .switchTab('Manager');

    calculationTableHelper.expectTableContains(memberCalculationTableData);
  });

  it('should cut list after 10 entries', () => {
    memberDetailPage.visit(1)
      .switchTab('Administrator');

    calculationTableHelper.expectLengthOfTable(10)
      .expectTableToBeExtendable()
      .toggleShowAll()
      .expectLengthOfTable(12)
      .expectTableToBeFoldable()
      .toggleShowAll();
  });

  it.only('should save a new calculation', () => {
    // go to tab
    memberDetailPage.visit(1)
      .switchTab('Administrator');

    // click button
    memberDetailPage.addCalculation()
      .click();

    // check save button is disabled
    calculationDetailViewPage.saveButton()
      .should('be.disabled');

    calculationDetailViewPage.statusDropdown()
      .should('contain', 'Entwurf');

    experienceTableHelper.expectLengthOfTable(13);
    degreeTableHelper.expectLengthOfTable(1);
    certificateTableHelper.expectLengthOfTable(1);
    leadershipExperienceTableHelper.expectEmptyTable();

    // todo check that points are zero at beginning

    // todo change status

    // click every radio button on a experience.
    experienceTableHelper.tableRows()
      .each(($row, index) => {
        // check if warning hint is there on expereinece.
        cy.wrap($row)
          .contains('mat-icon', 'warning')
          .should('be.visible');

        experienceTableHelper.selectRowRelevancy($row, Relevancy.STRONGLY);

        // check if the warning hint is gone after selection.
        experienceTableHelper.tableRows()
          .eq(index)
          .contains('mat-icon', 'warning')
          .should('not.exist');
      });

    degreeTableHelper.tableRows()
      .each(($row) => {
        // check if warning is there
        cy.wrap($row)
          .contains('mat-icon', 'warning')
          .should('be.visible');

        /*
         * todo check if all three procent input fields are disabled.
         * todo click first checkbox. Check if first field is enabled. To for all
         * todo set values that dont add up to 100
         */

        // expand Degree row.
        degreeTableHelper.expandRow($row);

        degreeTableHelper.setRowCheckboxRelevancy($row, Relevancy.NORMAL);

        // check if warning is there
        degreeTableHelper.setRowPercentage($row, Relevancy.NORMAL, 100);

        // check if warning is not there
        cy.wrap($row)
          .contains('mat-icon', 'warning')
          .should('not.exist');
      });

    // check save button is enabled
    calculationDetailViewPage.saveButton()
      .should('be.enabled');
  });

  it('should save a exsisting caluculation', () => {
    // todo add a new experience, degree, certificate and a leadership.

    // todo go to tab

    // todo click calculation.

    // todo check if the new values are not displayed.

    // todo change form

    // todo save
  });

  it('should only display leadership experiences in calculation from a Management role', () => {
    // todo go to tab (no m-role)

    // todo click add button

    // todo check that no leadership experience is present

    // todo go to tab (m-role)

    // todo click add button

    // todo check that leadership experience is present
  });
});
