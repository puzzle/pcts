import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';

describe('add certificate modal', () => {
  beforeEach(() => {
    memberDetailPage.visit(1);
  });

  it('should open correct modal', () => {
    modalPage.modalButton('add', 'certificate')
      .click();
    modalPage.modalTitle()
      .should('include.text', 'Zertifikat hinzufügen');
  });

  it('should create certificate', () => {
    modalPage.modalButton('add', 'certificate')
      .click();
    formPage.submitButtonShouldBe('disabled');
    formPage.type('certificateType', 'Mic');

    cy.get('mat-option')
      .contains('Microsoft Certified: Azure Administrator Associate')
      .click();

    formPage.submitButtonShouldBe('disabled');
    formPage.typeAndBlur('completedAt', '10.10.2000');
    formPage.submitButtonShouldBe('enabled');
    formPage.save();

    formPage.shouldShowSuccessToast('Zertifikat wurde erfolgreich erstellt.');
  });

  describe('show error when form field is not correct filled', () => {
    it('certificate type', () => {
      modalPage.modalButton('add', 'certificate')
        .click();
      formPage.submitButtonShouldBe('disabled');
      cy.getByTestId('certificateType')
        .focus()
        .blur();
      formPage.shouldShowValidationError('Muss ausgefühlt sein', 'certificateType');
      formPage.submitButtonShouldBe('disabled');
      formPage.type('certificateType', 'invalid entry');
      cy.getByTestId('certificateType')
        .blur();
      formPage.shouldShowValidationError('Ungültige Eingabe', 'certificateType');
    });

    it.only('completed at', () => {
      modalPage.modalButton('add', 'certificate')
        .click();
      // formPage.submitButtonShouldBe('disabled');
      formPage.typeAndBlur('completedAt', 'invalid entry');
      // cy.pause();
      formPage.shouldShowValidationError('Muss ausgefühlt sein\n' +
        'Ungültiges Datum', 'completedAt');
      formPage.submitButtonShouldBe('disabled');
    });
  });
});
