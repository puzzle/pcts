import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';

describe('Add leadershipExperience modal', () => {
  const openLeadershipExModal = () => {
    memberDetailPage.openModalButton('add', 'leadership-experience')
      .click();

    modalPage.checkModalIconButtonVisible();
  };

  beforeEach(() => {
    memberDetailPage.visit(1);
  });

  it('should open correct modal', () => {
    openLeadershipExModal();
    modalPage.modalTitle()
      .should('include.text', 'Führungserfahrung hinzufügen');
  });

  it.only('should create leadershipExperience', () => {
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
