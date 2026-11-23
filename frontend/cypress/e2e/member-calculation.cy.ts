import memberDetailPage from '../pages/memberDetailPage';
import { memberCalculationTableData } from '../support/helper/table-data';
import { TableHelper } from '../support/helper/table-helper';
import * as users from '../fixtures/users.json';
import calculationDetailViewPage from '../pages/calculationDetailViewPage';
import { Relevancy } from '../../src/app/features/calculations/relevancy.enum';
import { StringMatcher } from 'cypress/types/net-stubbing';
import formPage from '../pages/formPage';
import {
  createCalculation,
  createCertificate,
  createDegree,
  createExperience,
  createLeadershipExperience
} from '../support/helper/calculationHelper';


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

  it('should save a new calculation', () => {
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

    calculationDetailViewPage.pointsValue()
      .should('have.text', '0.00');

    calculationDetailViewPage.setStatus('Aktiv');

    // click every radio button on a experience.
    experienceTableHelper.tableRows()
      .each(($row, index) => {
        // check if warning hint is there on expereinece.
        experienceTableHelper.expectWarning($row);

        experienceTableHelper.selectRowRelevancy($row, Relevancy.STRONGLY);

        /*
         * check if the warning hint is gone after selection.
         * $row can't be used here because after page update it doesn't exsist anymore
         */
        experienceTableHelper.tableRows()
          .eq(index)
          .contains('mat-icon', 'warning')
          .should('not.exist');
      });

    degreeTableHelper.tableRows()
      .each(($row) => {
        // check if warning is there
        degreeTableHelper.expectWarning($row);

        // expand Degree row.
        degreeTableHelper.expandRow($row);

        [Relevancy.POORLY,
          Relevancy.NORMAL,
          Relevancy.STRONGLY].forEach((relevancy) => {
          degreeTableHelper.getPercentageInputField($row, relevancy)
            .should('be.disabled');
          degreeTableHelper.setRowCheckboxRelevancy($row, relevancy);
          degreeTableHelper.getPercentageInputField($row, relevancy)
            .should('be.enabled');
          degreeTableHelper.setRowCheckboxRelevancy($row, relevancy, false);
        });

        degreeTableHelper.setRowCheckboxRelevancy($row, Relevancy.NORMAL)
          .setRowPercentage($row, Relevancy.NORMAL, 100)
          .expectNoWarning($row)
          .setRowCheckboxRelevancy($row, Relevancy.STRONGLY)
          .setRowPercentage($row, Relevancy.STRONGLY, 100)
          .expectWarning($row)
          .setRowPercentage($row, Relevancy.STRONGLY, 50)
          .setRowPercentage($row, Relevancy.NORMAL, 50)
          .expectNoWarning($row);
      });

    // check save button is enabled
    calculationDetailViewPage.saveButton()
      .should('be.enabled');

    cy.intercept('POST', '**/api/v1/calculations')
      .as('createCalculation');

    calculationDetailViewPage.saveButton()
      .click();

    cy.wait('@createCalculation')
      .then((interception) => {
        expect(interception.response?.statusCode).to.eq(201);
      });


    formPage.shouldShowSuccessToast('PCTS-Berechnung wurde erfolgreich erstellt.');
  });

  it('should update an existing calculation (and verify snapshot behavior)', () => {
    let calculationBodyBefore: any;
    let token: string;

    const regexCalculationPath: StringMatcher = /\/api\/v1\/calculations\/\d+$/;
    const regexCalculationTemplatePath: StringMatcher = /\/api\/v1\/calculations\/template\/\d+\/\d+$/;

    cy.intercept({ method: 'GET',
      url: regexCalculationPath })
      .as('getCalculation');
    cy.intercept({ method: 'GET',
      url: regexCalculationTemplatePath })
      .as('getCalculationTemplate');
    cy.intercept({ method: 'GET',
      url: '**/api/v1/members/*' })
      .as('getMember');

    memberDetailPage.visit(2);

    cy.wait('@getMember')
      .then((interception) => {
        const authHeader = interception.request.headers['authorization'];
        const headerString = Array.isArray(authHeader) ? authHeader[0] : authHeader;
        token = headerString.split(' ')[1];

        createCalculation(token);
      });

    memberDetailPage.switchTab('Manager');

    calculationTableHelper.tableRows()
      .first()
      .click();

    cy.wait('@getCalculation')
      .then((interception) => {
        calculationBodyBefore = interception.response?.body;

        createExperience(token);
        createLeadershipExperience(token);
        createDegree(token);
        createCertificate(token);
      });

    calculationDetailViewPage.cancelButton()
      .click();
    memberDetailPage.switchTab('Manager');
    memberDetailPage.addCalculation()
      .click();

    cy.wait('@getCalculationTemplate')
      .then((interception) => {
        const calculationBodyAfter = interception.response?.body;
        expect(calculationBodyBefore).to.not.eql(calculationBodyAfter);
      });

    calculationDetailViewPage.cancelButton()
      .click();
    memberDetailPage.switchTab('Manager');
    calculationTableHelper.tableRows()
      .first()
      .click();

    cy.wait('@getCalculation')
      .then((interceptionAfter) => {
        const calculationBodyAfter = interceptionAfter.response?.body;
        expect(calculationBodyBefore).to.eql(calculationBodyAfter);
      });

    experienceTableHelper.tableRows()
      .each(($row, index) => {
        degreeTableHelper.expectNoWarning($row);

        experienceTableHelper.selectRowRelevancy($row, Relevancy.STRONGLY);

        experienceTableHelper.tableRows()
          .eq(index)
          .contains('mat-icon', 'warning')
          .should('not.exist');
      });

    degreeTableHelper.tableRows()
      .each(($row) => {
        degreeTableHelper.expandRow($row);

        degreeTableHelper.setRowCheckboxRelevancy($row, Relevancy.NORMAL);
        degreeTableHelper.setRowPercentage($row, Relevancy.NORMAL, 100);

        degreeTableHelper.expectWarning($row);

        degreeTableHelper.setRowCheckboxRelevancy($row, Relevancy.STRONGLY, false);

        degreeTableHelper.expectNoWarning($row);

        cy.intercept('PUT', '**/api/v1/calculations/*')
          .as('updateCalculation');

        calculationDetailViewPage.saveButton()
          .click();

        cy.wait('@updateCalculation')
          .then((interception) => {
            expect(interception.response?.statusCode).to.eq(200);
          });

        formPage.shouldShowSuccessToast('PCTS-Berechnung wurde erfolgreich aktualisiert.');
      });
  });

  it('should only display leadership experiences in calculation from a Management role', () => {
    memberDetailPage.visit(1)
      .switchTab('Administrator');

    memberDetailPage.addCalculation()
      .click();

    leadershipExperienceTableHelper.expectEmptyTable();

    calculationDetailViewPage.cancelButton()
      .click();

    memberDetailPage.switchTab('Intern');

    memberDetailPage.addCalculation()
      .click();

    leadershipExperienceTableHelper.expectTableNotFound();
  });

  describe('Read-Only When Archived Calculation', () => {
    it('should disable all form fields when opening an archived calculation', () => {
      memberDetailPage.visit(1)
        .switchTab('Administrator');

      calculationTableHelper
        .toggleShowAll()
        .getTableRowBy(13, 'Archiviert')
        .click();

      calculationDetailViewPage.statusDropdown()
        .should('be.disabled');

      experienceTableHelper.tableRows()
        .each(($row) => {
          experienceTableHelper.expectRadioRowsIs($row, 'disabled');
        });

      degreeTableHelper.tableRows()
        .each(($row) => {
          degreeTableHelper.expectCheckboxRowIs($row, 'disabled');
        });
    });

    it('should not appear the Save and Cancel buttons when opening an archived calculation', () => {
      memberDetailPage.visit(1)
        .switchTab('Administrator');

      calculationTableHelper
        .toggleShowAll()
        .getTableRowBy(11, 'Archiviert')
        .click();

      calculationDetailViewPage.saveButton()
        .should('not.exist');
      calculationDetailViewPage.cancelButton()
        .should('not.exist');
    });

    it('should not be disabled when opening a non-archived calculation', () => {
      memberDetailPage.visit(1)
        .switchTab('Administrator');

      calculationTableHelper
        .toggleShowAll()
        .getTableRowBy(10, 'Aktiv')
        .click();
    });

    calculationDetailViewPage.statusDropdown()
      .should('be.not.disabled');

    experienceTableHelper.tableRows()
      .each(($row) => {
        experienceTableHelper.expectRadioRowsIs($row, 'not.disabled');
      });

    degreeTableHelper.tableRows()
      .each(($row) => {
        degreeTableHelper.expectCheckboxRowIs($row, 'not.disabled');
      });

    calculationDetailViewPage.saveButton()
      .should('exist');
    calculationDetailViewPage.cancelButton()
      .should('exist');
  });
});
