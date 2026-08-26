import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import * as users from '../fixtures/users.json';
import formPage from '../pages/formPage';

describe('Add experience Modal', () => {
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

  ['ENTER_ANOTHER',
    'COPY'].forEach((buttonType: string) => {
    it(`should create experience via ${buttonType}`, () => {
      cy.intercept('api/v1/experiences')
        .as('experiences');

      openExperienceModal();


      formPage.submitButtonShouldBe('disabled');
      modalPage.modalTitle()
        .should('include.text', 'Berufs- und Lebenserfahrung hinzufügen');

      cy.getByTestId('experienceType')
        .type('Pra');

      cy.get('mat-option')
        .contains('Praktikum')
        .click();

      formPage.typeAndBlur('startDate', '10.10.2023');
      formPage.typeAndBlur('endDate', '10.12.2027');
      formPage.typeAndBlur('name', 'Software Engineer');
      formPage.typeAndBlur('employer', 'TechNova Solutions');
      formPage.typeAndBlur('percent', '100');
      formPage.typeAndBlur('comment', 'Worked on backend APIs and DevOps tasks.');
      formPage.submitButtonShouldBe('enabled');
      formPage.clickSubmitMenuItem(buttonType);


      formPage.shouldShowSuccessToast('Berufs- und Lebenserfahrung wurde erfolgreich erstellt.');

      cy.get('@experiences')
        .then((interception) => {
          expect(interception.request.body).to.contain({
            name: 'Software Engineer',
            memberId: 1,
            experienceTypeId: 1,
            comment: 'Worked on backend APIs and DevOps tasks.',
            percent: 100,
            startDate: '2023-10-10',
            endDate: '2027-12-10'
          });
        });
      modalPage.checkModalIsClosed();
    });
  });
  // test both alternative save buttons

  describe('Validation Errors', () => {
    beforeEach(() => {
      openExperienceModal();
    });

    it('validates experience type requirement and input', () => {
      formPage.submitButtonShouldBe('disabled');
      cy.getByTestId('experienceType')
        .clear()
        .blur();
      formPage.shouldShowValidationError('Muss ausgefüllt sein', 'experienceType');
      formPage.typeAndBlur('experienceType', 'invalid entry');
      formPage.shouldShowValidationError('Ungültige Eingabe', 'experienceType');
    });

    const fields = {
      startDate: ['Muss ausgefüllt sein',
        'Ungültiges Datum'],
      endDate: ['Ungültiges Datum']
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
    it('should show error when startDate is after endDate', () => {
      openExperienceModal();
      formPage.type('experienceType', 'Pra');
      cy.get('mat-option')
        .contains('Praktikum')
        .click();

      formPage.typeAndBlur('name', 'Software Engineer');
      formPage.typeAndBlur('employer', 'TechNova Solutions');
      formPage.typeAndBlur('percent', '100');
      formPage.typeAndBlur('startDate', '10.10.2000');
      formPage.typeAndBlur('endDate', '10.09.2000');

      formPage.submitButtonShouldBe('enabled');
      formPage.save();

      formPage.shouldShowErrorToast('Von mit dem Wert 2000-10-10 muss jünger sein als 2000-09-10.');
    });
  });

  describe('Closing Modal', () => {
    beforeEach(() => {
      openExperienceModal();
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
