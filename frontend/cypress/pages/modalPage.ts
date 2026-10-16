import { Page } from './page';
import formPage from './formPage';

class ModalPage extends Page {
  modalTitle() {
    return cy.getByTestId('modal-title');
  }

  checkModalIsClosed() {
    cy.get('.mat-dialog-container')
      .should('not.exist');
  }

  selectAutoCompleteValue = (modelName: string, value: string) => {
    formPage.type(`${modelName}`, value.slice(0, 3));
    cy.get('mat-option')
      .contains(value)
      .click();
  };

  selectTextField = (textFieldName: string, value: string) => {
    formPage.type(`${textFieldName}`, value);
  };

  checkModalIconButtonVisible() {
    cy.getByTestId('close-modal-icon-button')
      .should('be.visible');
  }

  checkModalIconButtonFocused() {
    cy.getByTestId('close-modal-icon-button')
      .should('be.focused');
  }

  /*
   * the dialog moves focus to its first tabbable element once the open animation is done,
   * focusing a field before that loses the focus again
   */
  waitUntilOpened() {
    this.checkModalIconButtonVisible();
    this.checkModalIconButtonFocused();
  }
}
export default new ModalPage();
