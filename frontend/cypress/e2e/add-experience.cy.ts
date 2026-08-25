import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import * as users from '../fixtures/users.json';

describe('Add degree Modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  const openExperienceModal = () => {
    memberDetailPage.openModalButton('add', 'experience')
      .click();

    modalPage.checkModalIconButtonVisible();
  };

  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });
  it('should open correct modal', () => {
    openExperienceModal();
    modalPage.modalTitle()
      .should('include.text', 'Berufs- und Lebenserfahrung hinzufügen');
  });
});
