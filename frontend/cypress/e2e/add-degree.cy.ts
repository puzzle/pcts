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


  it.only('should create degree', () => {
    cy.intercept('api/v1/degrees')
      .as('degrees');
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
    /*
     * check request is made with the proper request body
     *  https://docs.cypress.io/api/commands/request#Alias-the-request-using-as
     */
    cy.get('@degrees')
      .then((interception) => {
        const body = interception.request.body;

        const expectedValue = {
          name: 'Mathematik',
          memberId: 1,
          typeId: 1,
          institution: 'GIBB',
          completed: true,
          comment: '',
          startDate: '2000-10-10',
          endDate: '2001-12-10'
        };
        expect(body).to.contain(expectedValue);
      });
    modalPage.checkModalIsClosed();
  });

  // test both alternative save buttons

  describe('Validation Errors', () => {
    beforeEach(() => {
      openDegreeModal();
    });

    it('validates degree type requirement and input', () => {
      formPage.submitButtonShouldBe('disabled');
      cy.getByTestId('degreeType')
        .focus()
        .type('something')
        .clear()
        .blur();
      formPage.shouldShowValidationError('Muss ausgefüllt sein', 'degreeType');

      formPage.type('degreeType', 'invalid entry');
      cy.getByTestId('degreeType')
        .blur();
      formPage.shouldShowValidationError('Ungültige Eingabe', 'degreeType');
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

