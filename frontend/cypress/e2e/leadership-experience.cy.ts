import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';
import * as users from '../fixtures/users.json';

describe('LeadershipExperience modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });
  describe('Add leadershipExperience modal', () => {
    const openLeadershipExModal = () => {
      memberDetailPage.openModalButton('add', 'leadership-experience')
        .click();

      modalPage.checkModalIconButtonVisible();
    };

    it('should open correct modal', () => {
      openLeadershipExModal();
      modalPage.modalTitle()
        .should('include.text', 'Führungserfahrung hinzufügen');
    });

    it('should create leadershipExperience', () => {
      openLeadershipExModal();

      formPage.submitButtonShouldBe('disabled');
      modalPage.selectAutoCompleteValue('leadershipExperienceType', 'Leader Essentials');
      modalPage.selectTextField('comment', 'This is a comment');

      formPage.submitButtonShouldBe('enabled');
      formPage.save();

      formPage.shouldShowSuccessToast('Führungserfahrung wurde erfolgreich erstellt.');
      modalPage.checkModalIsClosed();
    });

    describe('Validation Errors', () => {
      beforeEach(() => {
        openLeadershipExModal();
      });

      it('validates leadershipExperience type requirement and input', () => {
        formPage.submitButtonShouldBe('disabled');
        cy.getByTestId('leadershipExperienceType')
          .focus()
          .blur();
        formPage.shouldShowValidationError('Muss ausgefüllt sein', 'leadershipExperienceType');

        formPage.type('leadershipExperienceType', 'invalid entry');
        cy.getByTestId('leadershipExperienceType')
          .focus()
          .blur();
        formPage.shouldShowValidationError('Ungültige Eingabe', 'leadershipExperienceType');
      });
    });

    describe('Closing Modal', () => {
      beforeEach(() => {
        openLeadershipExModal();
      });

      ['icon-button',
        'button'].forEach((buttonType: string) => {
        it(`closes via ${buttonType}`, () => {
          cy.getByTestId(`close-modal-${buttonType}`)
            .click();
          modalPage.checkModalIsClosed();
        });
      });
    });
  });
  describe('Edit leadershipExperience modal', () => {
    const openLeadershipExperienceModal = () => {
      cy.getByTestId('cv-table-leadership-experience')
        .within(() => {
          cy.getByTestId('generic-table-cell')
            .eq(1)
            .click();
        });
    };

    beforeEach(() => {
      openLeadershipExperienceModal();
    });

    it('should open correct modal', () => {
      modalPage.modalTitle()
        .should('include.text', 'Führungserfahrung bearbeiten');
    });

    it('should save changes correctly', () => {
      cy.intercept('PUT', 'api/v1/leadership-experiences/**')
        .as('leadershipExperiences');

      formPage.clearAndBlur('leadershipExperienceType');
      modalPage.selectAutoCompleteValue('leadershipExperienceType', 'Leader Essentials');

      formPage.clearAndBlur('comment');
      formPage.typeAndBlur('comment', 'This is a comment.');

      cy.getByTestId('submit-button')
        .click();

      formPage.shouldShowSuccessToast('Führungserfahrung wurde erfolgreich aktualisiert.');

      cy.wait('@leadershipExperiences');

      cy.get('@leadershipExperiences')
        .then((interception) => {
          expect(interception.request.body).to.contain({
            memberId: 1,
            leadershipExperienceTypeId: 3,
            comment: 'This is a comment.'
          });
        });
      modalPage.checkModalIsClosed();
    });

    it('should open the edit modal and delete the modal', () => {
      cy.getByTestId('delete-button')
        .click();
      formPage.shouldShowSuccessToast('Führungserfahrung wurde erfolgreich gelöscht.');
      modalPage.checkModalIsClosed();
    });
  });
});
