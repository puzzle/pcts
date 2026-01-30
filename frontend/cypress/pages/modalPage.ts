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
    formPage.type(`${modelName}`, modelName.slice(0, 3));
    cy.get('mat-option')
      .contains(value)
      .click();
  };

  checkModalIconButtonVisible() {
    cy.getByTestId('close-modal-icon-button')
      .should('be.visible')
      .and('be.focused');
  }
}

export default new ModalPage();
