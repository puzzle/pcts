import { errorColor, successColor } from '../support/helper/colors';

class FormPage {
  visitAdd(modelPathName: string) {
    cy.visit(`/${modelPathName}/add`);
    return this;
  }

  visitEdit(modelId: number | string, modelPathName: string) {
    cy.visit(`/${modelPathName}/${modelId}/edit`);
    return this;
  }

  get submitButton() {
    return cy.getByTestId('submit-button');
  }

  save() {
    this.submitButton.click();
    return this;
  }

  clearAndBlur(fieldName: string) {
    cy.getByTestId(fieldName)
      .type('123')
      .clear()
      .blur();
  }

  typeAndBlur(fieldName: string, text: string) {
    cy.getByTestId(fieldName)
      .type(text)
      .blur();
    return this;
  }

  shouldHaveTitle(expectedTitle: string, modelName: string) {
    cy.getByTestId('title')
      .should('include.text', modelName + ' ' + expectedTitle);
    return this;
  }

  submitButtonShouldBe(state: 'enabled' | 'disabled') {
    this.submitButton.should(`be.${state}`);
  }

  shouldShowValidationError(message: string, fieldName: string) {
    cy.getByTestId('validation-error')
      .should('include.text', message)
      .parents('mat-form-field')
      .find('input')
      .invoke('attr', 'data-testid')
      .should('eq', fieldName);
    return this;
  }


  shouldHaveFieldValue(fieldName: string, value: string) {
    cy.getByTestId(fieldName)
      .should('have.value', value);
  }

  shouldShowErrorToast(message: string) {
    cy.getByTestId('toaster-message')
      .should('include.text', message)
      .should('have.css', 'background-color')
      .and('eq', errorColor());
  }

  shouldShowSuccessToast(message: string) {
    cy.getByTestId('toaster-message')
      .should('include.text', message)
      .should('have.css', 'background-color')
      .and('eq', successColor());
  }
}

export default new FormPage();
