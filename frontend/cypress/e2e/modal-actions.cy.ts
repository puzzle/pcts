import * as users from '../fixtures/users.json';
import memberDetailPage from '../pages/memberDetailPage';

describe('ModalActions', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  it('should open the edit modal and show the modal actions', () => {

  });
  it('should display the cancel button', () => {
    cy.contains('button', 'Cancel')
      .should('be.visible');
  });

  it('should display the submit button', () => {
    cy.contains('button', 'Action')
      .should('be.visible');
  });

  it('should display the delete button', () => {
    cy.contains('button', 'Delete')
      .should('be.visible');
  });

  it('should click cancel', () => {
    cy.contains('button', 'Cancel')
      .click();
  });

  it('should click submit', () => {
    cy.contains('button', 'Action')
      .click();
  });

  it('should click delete', () => {
    cy.contains('button', 'Delete')
      .click();
  });
});
