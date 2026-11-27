import { TableHelper } from '../support/helper/table-helper';
import * as users from '../fixtures/users.json';
import memberDetailPage from '../pages/memberDetailPage';
import calculationDetailViewPage from '../pages/calculationDetailViewPage';

describe('Member Calculation Overview', () => {
  describe('Admin', () => {
    const calculationTableHelper = TableHelper.withTableTestId('member-calculation-table');

    beforeEach(() => {
      cy.loginAsUser(users.gl);
      memberDetailPage.visit(1)
        .switchTab('Administrator');
    });

    it('should display table with all calculations', () => {
      cy.getByTestId('generic-table')
        .should('be.visible');
      cy.getByTestId('add-calculation-button')
        .should('be.visible');
      calculationTableHelper.expectLengthOfTable(10);
    });
  });

  describe('Not Admin', () => {
    const experienceTableHelper = TableHelper.withTableTestId('experience-table');
    const degreeTableHelper = TableHelper.withTableTestId('degree-table');

    const expectedTexts = [
      'PCTS-Berechnung',
      'PCTS-Punkte',
      '38.26',
      'Berufs- und Lebenserfahrungen',
      'Ausbildungen',
      'Zertifikate',
      'Führungserfahrungen'
    ];

    beforeEach(() => {
      cy.loginAsUser(users.member);
      memberDetailPage.visit(1)
        .switchTab('Administrator');
    });

    it('should display calculation in read-only mode', () => {
      expectedTexts.forEach((text) => {
        cy.contains(text)
          .should('be.visible');
      });

      calculationDetailViewPage.statusDropdownShouldBeDisabled();

      experienceTableHelper.expectRadioTableRowsToBe('disabled');

      degreeTableHelper.expectCheckboxTableRowsToBe('disabled');

      calculationDetailViewPage.saveButton()
        .should('not.exist');
      calculationDetailViewPage.cancelButton()
        .should('not.exist');
    });
  });
});
