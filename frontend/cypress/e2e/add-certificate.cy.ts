import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';

describe('Add Certificate Modal', () => {
  const openCertificateModal = () => {
    modalPage.modalButton('add', 'certificate')
      .click();
    cy.getByTestId('Zertifikat-close-dialog')
      .should('be.visible')
      .and('be.focused');
  };

  beforeEach(() => {
    memberDetailPage.visit(1);
  });

  it('should open correct modal', () => {
    openCertificateModal();
    modalPage.modalTitle()
      .should('include.text', 'Zertifikat hinzufügen');
  });

  it('should create certificate', () => {
    openCertificateModal();

    formPage.submitButtonShouldBe('disabled');
    formPage.type('certificateType', 'Mic');

    cy.get('mat-option')
      .contains('Microsoft Certified: Azure Administrator Associate')
      .click();

    formPage.typeAndBlur('completedAt', '10.10.2000');
    formPage.submitButtonShouldBe('enabled');
    formPage.save();

    formPage.shouldShowSuccessToast('Zertifikat wurde erfolgreich erstellt.');
    modalPage.shouldCloseModal();
  });

  describe('Validation Errors', () => {
    beforeEach(() => {
      openCertificateModal();
    });

    it('validates certificate type requirement and input', () => {
      formPage.submitButtonShouldBe('disabled');
      cy.getByTestId('certificateType')
        .focus()
        .blur();
      formPage.shouldShowValidationError('Muss ausgefühlt sein', 'certificateType');

      formPage.type('certificateType', 'invalid entry');
      cy.getByTestId('certificateType')
        .blur();
      formPage.shouldShowValidationError('Ungültige Eingabe', 'certificateType');
    });

    ['completedAt',
      'validUntil'].forEach((fieldName) => {
      it(`shows error for invalid date in: ${fieldName}`, () => {
        formPage.submitButtonShouldBe('disabled');
        formPage.typeAndBlur(fieldName, 'invalid entry');

        if (fieldName === 'completedAt') {
          formPage.shouldShowValidationError('Muss ausgefühlt sein', fieldName);
        }
        formPage.shouldShowValidationError('Ungültiges Datum', fieldName);
        formPage.submitButtonShouldBe('disabled');
      });
    });
  });

  describe('Error Toasts', () => {
    it('should show error when completedAt is after validUntil', () => {
      cy.on('uncaught:exception', (err) => {
        return !(err.message.includes('Http failure response') || err.message.includes('400'));
      });

      openCertificateModal();
      formPage.type('certificateType', 'Mic');
      cy.get('mat-option')
        .contains('Microsoft Certified: Azure Administrator Associate')
        .click();

      formPage.typeAndBlur('completedAt', '10.10.2000');
      formPage.typeAndBlur('validUntil', '10.09.2000');

      formPage.submitButtonShouldBe('enabled');
      formPage.save();

      formPage.shouldShowErrorToast('Datum mit dem Wert 2000-10-10 muss jünger sein als 2000-09-10.');
    });
  });

  describe('Closing Modal', () => {
    beforeEach(() => {
      openCertificateModal();
    });

    it('closes via the close icon', () => {
      cy.getByTestId('Zertifikat-close-dialog')
        .click();
      modalPage.shouldCloseModal();
    });

    it('closes via the cancel button', () => {
      cy.getByTestId('certificate-cancel')
        .click();
      modalPage.shouldCloseModal();
    });
  });
});
