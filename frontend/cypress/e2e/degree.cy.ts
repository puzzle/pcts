import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';
import * as users from '../fixtures/users.json';

describe('Degree Modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
    memberDetailPage.visit(1);
  });

  describe('Add degree Modal', () => {
    const openDegreeModal = () => {
      memberDetailPage.openModalButton('add', 'degree')
        .click();

      modalPage.checkModalIconButtonVisible();
    };

    it('should open correct modal', () => {
      openDegreeModal();
      modalPage.modalTitle()
        .should('include.text', 'Ausbildung hinzufügen');
    });


    ['ENTER_ANOTHER',
      'COPY'].forEach((buttonType: string) => {
      it(`should create degree via ${buttonType}`, () => {
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
        formPage.clickSubmitMenuItem(buttonType);

        modalPage.modalTitle()
          .should('include.text', 'Ausbildung hinzufügen');

        formPage.shouldShowSuccessToast('Ausbildung wurde erfolgreich erstellt.');
        /*
         * check request is made with the proper request body
         *  https://docs.cypress.io/api/commands/request#Alias-the-request-using-as
         */
        cy.get('@degrees')
          .then((interception) => {
            expect(interception.request.body).to.contain({
              name: 'Mathematik',
              memberId: 1,
              typeId: 1,
              institution: 'GIBB',
              completed: true,
              comment: '',
              startDate: '2000-10-10',
              endDate: '2001-12-10'
            });
          });
        modalPage.checkModalIsClosed();
      });
    });

    describe('Validation Errors', () => {
      beforeEach(() => {
        openDegreeModal();
      });

      it('validates degree type requirement and input', () => {
        formPage.submitButtonShouldBe('disabled');
        modalPage.selectAutoCompleteValue('degreeType', 'Bachelor\'s Degree');
        cy.getByTestId('degreeType')
          .clear()
          .blur();
        formPage.shouldShowValidationError('Muss ausgefüllt sein', 'degreeType');
        formPage.typeAndBlur('degreeType', 'invalid entry');
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
            cy.wait(300);
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

    it('should open correct modal', () => {
      modalPage.modalTitle()
        .should('include.text', 'Ausbildung bearbeiten');
    });

    it('should save changes correctly', () => {
      cy.intercept('PUT', 'api/v1/degrees/**')
        .as('degrees');

      formPage.clearAndBlur('degreeType');
      modalPage.selectAutoCompleteValue('degreeType', 'Bachelor\'s Degree');

      formPage.clearAndBlur('name');
      formPage.typeAndBlur('name', 'Bachelor in mathematics');

      formPage.clearAndBlur('institution');
      formPage.typeAndBlur('institution', 'GIBB');

      formPage.clearAndBlur('startDate');
      formPage.typeAndBlur('startDate', '10.10.2000');

      formPage.clearAndBlur('endDate');
      formPage.typeAndBlur('endDate', '10.12.2001');

      formPage.submitButtonShouldBe('enabled');

      cy.getByTestId('submit-button')
        .click();

      formPage.shouldShowSuccessToast('Ausbildung wurde erfolgreich aktualisiert.');

      cy.wait('@degrees');

      cy.get('@degrees')
        .then((interception) => {
          expect(interception.request.body).to.contain({
            name: 'Bachelor in mathematics',
            memberId: 1,
            typeId: 1,
            institution: 'GIBB',
            completed: true,
            comment: '',
            startDate: '2000-10-10',
            endDate: '2001-12-10'
          });
        });
      modalPage.checkModalIsClosed();
    });

    it('should open the edit modal and delete the modal', () => {
      cy.getByTestId('delete-button')
        .click();
      formPage.shouldShowSuccessToast('Ausbildung wurde erfolgreich gelöscht.');
      modalPage.checkModalIsClosed();
    });
  });
});
