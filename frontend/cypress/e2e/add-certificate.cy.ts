import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';
import * as users from '../fixtures/users.json';

describe('Add certificate Modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  const openCertificateModal = () => {
    memberDetailPage.openModalButton('add', 'certificate')
      .click();

    modalPage.checkModalIconButtonVisible();
  };

  beforeEach(() => {
    cy.loginAsUser(users.gl);
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
    modalPage.selectAutoCompleteValue('certificateType', 'Microsoft Certified: Azure Administrator Associate');

    formPage.typeAndBlur('completedAt', '10.10.2000');
    formPage.submitButtonShouldBe('enabled');
    formPage.save();

    formPage.shouldShowSuccessToast('Zertifikat wurde erfolgreich erstellt.');
    modalPage.checkModalIsClosed();
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
      formPage.shouldShowValidationError('Muss ausgefüllt sein', 'certificateType');

      formPage.type('certificateType', 'invalid entry');
      cy.getByTestId('certificateType')
        .blur();
      formPage.shouldShowValidationError('Ungültige Eingabe', 'certificateType');
    });

    const fields = {
      completedAt: ['Muss ausgefüllt sein',
        'Ungültiges Datum'],
      validUntil: ['Ungültiges Datum']
    };

    Object.entries(fields)
      .forEach(([fieldName,
        errors]) => {
        it(`shows error for invalid date in: ${fieldName}`, () => {
          formPage.submitButtonShouldBe('disabled');

          formPage.typeAndBlur(fieldName, 'invalid entry');

          errors.forEach((error) => {
            formPage.shouldShowValidationError(error, fieldName);
          });
          formPage.submitButtonShouldBe('disabled');
        });
      });
  });

  describe('Error Toasts', () => {
    it('should show error when completedAt is after validUntil', () => {
      openCertificateModal();
      formPage.type('certificateType', 'Mic');
      cy.get('mat-option')
        .contains('Microsoft Certified: Azure Administrator Associate')
        .click();

      formPage.typeAndBlur('completedAt', '10.10.2000');
      formPage.typeAndBlur('validUntil', '10.09.2000');

      formPage.submitButtonShouldBe('enabled');
      formPage.save();

      formPage.shouldShowErrorToast('Abgeschlossen am mit dem Wert 2000-10-10 muss jünger sein als 2000-09-10.');
    });
  });

  describe('Closing Modal', () => {
    beforeEach(() => {
      openCertificateModal();
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
  describe('Edit certificate modal', () => {
    const openCertificateModal = () => {
      cy.getByTestId('cv-table-certificate')
        .within(() => {
          cy.getByTestId('generic-table-cell')
            .eq(1)
            .click();
        });
    };

    beforeEach(() => {
      openCertificateModal();
    });

    it('should open correct modal', () => {
      modalPage.modalTitle()
        .should('include.text', 'Zertifikat bearbeiten');
    });

    it('should save changes correctly', () => {
      cy.intercept('PUT', 'api/v1/certificates/**')
        .as('certificates');

      formPage.clearAndBlur('certificateType');
      modalPage.selectAutoCompleteValue('certificateType', 'Microsoft Certified: Azure Administrator Associate');

      formPage.clearAndBlur('completedAt');
      formPage.typeAndBlur('completedAt', '10.10.2023');

      formPage.clearAndBlur('validUntil');
      formPage.typeAndBlur('validUntil', '10.12.2027');

      formPage.clearAndBlur('comment');
      formPage.typeAndBlur('comment', 'This is a comment.');

      cy.getByTestId('submit-button')
        .click();

      formPage.shouldShowSuccessToast('Zertifikat wurde erfolgreich aktualisiert.');

      cy.wait('@certificates');

      cy.get('@certificates')
        .then((interception) => {
          expect(interception.request.body).to.contain({
            memberId: 1,
            certificateTypeId: 10,
            completedAt: '2023-10-10',
            validUntil: '2027-12-10',
            comment: 'This is a comment.'
          });
        });
      modalPage.checkModalIsClosed();
    });
  });
});
