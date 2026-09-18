// Only one preparation file will be deleted later when the other modals are finished and the tests have been copied over and changed to fit
import modalPage from '../pages/modalPage';
import memberDetailPage from '../pages/memberDetailPage';
import * as users from '../fixtures/users.json';
import formPage from '../pages/formPage';

describe('Degree Modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  describe('Edit degree modal', () => {
    const openDegreeModal = () => {
      cy.getByTestId('cv-table-degree')
        .within(() => {
          cy.getByTestId('generic-table-cell')
            .eq(1)
            .click();
        });

      modalPage.checkModalIconButtonVisible();
    };

    beforeEach(() => {
      openDegreeModal();
    });

    it('should open the edit modal and delete the modal', () => {
      cy.getByTestId('delete-button')
        .click();
      formPage.shouldShowSuccessToast('Ausbildung wurde erfolgreich gelöscht.');
      modalPage.checkModalIsClosed();
    });
  });
});
