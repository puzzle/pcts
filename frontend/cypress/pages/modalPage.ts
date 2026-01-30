import { Page } from './page';
import formPage from './formPage';

class ModalPage extends Page {
  modalTitle() {
    return cy.getByTestId('modal-title');
  }

  isModalClosed() {
    cy.get('.mat-dialog-container')
      .should('not.exist');
  }

  selectModelTypeValue = (modelName: string, value: string) => {
    formPage.type(`${modelName}Type`, modelName.slice(0, 3));
    cy.get('mat-option')
      .contains(value)
      .click();
  };
}

export default new ModalPage();
