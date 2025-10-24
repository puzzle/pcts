class FormPage {
  visitAdd() {
    cy.visit('/member/add');
    return this;
  }

  visitEdit(memberId: number | string) {
    cy.visit(`/member/${memberId}/edit`);
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

  shouldHaveTitle(expectedTitle: string) {
    cy.getByTestId('title')
      .should('include.text', 'Member ' + expectedTitle);
    return this;
  }

  submitButtonShouldBe(state: 'enabled' | 'disabled') {
    this.submitButton.should(`be.${state}`);
  }

  shouldShowValidationError(message: string) {
    cy.getByTestId('validation-error')
      .should('include.text', message);
    return this;
  }

  shouldHaveFieldValue(fieldName: string, value: string) {
    cy.getByTestId(fieldName)
      .should('have.value', value);
  }
}

export default new FormPage();
