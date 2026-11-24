import { Page } from './page';

class CalculationDetailViewPage extends Page {
  pointsValue() {
    return cy.get('.points');
  }

  statusDropdown() {
    return cy.get('#status');
  }

  experienceTable() {
    return cy.getByTestId('experience-table');
  }

  degreeTable() {
    return cy.getByTestId('degree-table');
  }

  certificateTable() {
    return cy.getByTestId('certificate-table');
  }

  leadershipExperienceTable() {
    return cy.getByTestId('leadership-experience-table');
  }

  saveButton() {
    return cy.getByTestId('calculation-save-button');
  }

  cancelButton() {
    return cy.getByTestId('calculation-cancel-button');
  }

  setStatus(status: 'Entwurf' | 'Aktiv' | 'Archiviert') {
    this.statusDropdown()
      .click();

    cy.get('mat-option')
      .contains(status)
      .click();
  }

  statusDropdownShouldBeDisabled() {
    this.statusDropdown()
      .should('have.class', 'mat-mdc-select-disabled');
  }

  statusDropdownShouldBeEnabled() {
    this.statusDropdown()
      .should('not.have.class', 'mat-mdc-select-disabled');
  }
}

export default new CalculationDetailViewPage();
