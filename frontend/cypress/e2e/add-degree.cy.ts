import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';
import * as users from '../fixtures/users.json';

describe('Add degree Modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  const openDegreeModal = () => {
    memberDetailPage.openModalButton('add', 'degree')
      .click();

    modalPage.checkModalIconButtonVisible();
  };

  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  it('should open correct modal', () => {
    openDegreeModal();
    modalPage.modalTitle()
      .should('include.text', 'Ausbildung hinzufügen');
  });

  it('should create degree', () => {
    openDegreeModal();

    formPage.submitButtonShouldBe('disabled');
    modalPage.selectAutoCompleteValue('degreeType', 'Bachelor\'s Degree');

    formPage.typeAndBlur('startDate', '10.10.2000');
    formPage.typeAndBlur('endDate', '10.12.2001');
    formPage.typeAndBlur('name', 'Mathematik');
    formPage.typeAndBlur('institution', 'GIBB');
    formPage.submitButtonShouldBe('enabled');
    formPage.save();


    formPage.shouldShowSuccessToast('Ausbildung wurde erfolgreich erstellt.');
    modalPage.checkModalIsClosed();
  });

  describe('Validation Errors', () => {
    beforeEach(() => {
      openDegreeModal();
    });

    it('validates degree type requirement and input', () => {
      formPage.submitButtonShouldBe('disabled');
      cy.getByTestId('degreeType')
        .focus()
        .blur();
      formPage.shouldShowValidationError('Muss ausgefüllt sein', 'degreeType');

      formPage.type('degreeType', 'invalid entry');
      cy.getByTestId('degreeType')
        .blur();
      formPage.shouldShowValidationError('Ungültige Eingabe', 'degreeType');
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
      openDegreeModal();
      formPage.type('degreeType', 'Mic');
      cy.get('mat-option')
        .contains('Bachelor\'s Degree')
        .click();

      formPage.typeAndBlur('startDate', '10.10.2000');
      formPage.typeAndBlur('endDate', '10.12.2001');

      formPage.submitButtonShouldBe('enabled');
      formPage.save();

      formPage.shouldShowErrorToast('Abgeschlossen am mit dem Wert 2000-10-10 muss jünger sein als 2000-09-10.');
    });
  });

  describe('Closing Modal', () => {
    beforeEach(() => {
      openDegreeModal();
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
