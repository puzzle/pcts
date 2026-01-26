import { Page } from './page';

class ModalPage extends Page {
  modalButton(mode: string, modelName: string) {
    return cy.getByTestId(`${mode}-${modelName}-button`);
  }

  modalTitle() {
    return cy.getByTestId('modal-title');
  }
}

export default new ModalPage();
