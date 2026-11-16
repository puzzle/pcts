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
    return cy.get('footer.bottom button')
      .last();
  }
}

export default new CalculationDetailViewPage();
