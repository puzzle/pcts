import {Page} from './page';

class FormPage extends Page {
  visitAdd() {
    cy.visit('/member/add');
  }

  visitEdit() {
    cy.visit('/member/1/edit');
  }

  addTextToFieldAndCheckButtonState(fieldName: string, fieldValue: string) {
    cy.getByTestId(fieldName).type(fieldValue);
    this.isSaveDisabled()
  }

  isSaveDisabled() {
    cy.getByTestId('submit-button').should('be.disabled');
  }
}

export default new FormPage()
