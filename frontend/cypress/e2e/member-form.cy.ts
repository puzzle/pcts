import FormPage from '../pages/formPage';
import translations from '../../public/i18n/de.json';
import OverviewPage from '../pages/overviewPage';
import { format } from 'date-fns';
import * as users from '../fixtures/users.json';


describe('MemberFormComponent', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  describe('member form component', () => {
    beforeEach(() => {
      FormPage.visitAdd('member');
    });
    [
      'firstName',
      'lastName',
      'birthDate',
      'employmentState'
    ].forEach((fieldName) => {
      it(`should show required error for field ${fieldName}`, () => {
        FormPage.clearAndBlur(fieldName);

        FormPage.shouldShowValidationError(translations.VALIDATION.REQUIRED, fieldName);
      });
    });

    ['employmentState',
      'organisationUnit'].forEach((fieldName) => {
      it(`should show invalid entry error for field ${fieldName}`, () => {
        FormPage.typeAndBlur(fieldName, 'invalid entry');

        FormPage.shouldShowValidationError(translations.VALIDATION.INVALID_ENTRY, fieldName);
      });
    });

    ['birthDate',
      'dateOfHire'].forEach((fieldName) => {
      it(`should show invalid date error for field ${fieldName}`, () => {
        FormPage.typeAndBlur(fieldName, 'invalid entry');

        FormPage.shouldShowValidationError(translations.VALIDATION.MATDATEPICKERPARSE, fieldName);
      });
    });

    ['birthDate'].forEach((fieldName) => {
      it(`should show date must be in past for field ${fieldName}`, () => {
        FormPage.typeAndBlur(fieldName, format(new Date(), 'dd.MM.yyyy'));

        FormPage.shouldShowValidationError(translations.VALIDATION.DATE_IS_NOT_IN_PAST, fieldName);
      });
    });

    it('should activate submit button when everything is filled out', () => {
      FormPage.submitButtonShouldBe('disabled');
      FormPage.typeAndBlur('firstName', 'John')
        .submitButtonShouldBe('disabled');
      FormPage.typeAndBlur('lastName', 'Doe')
        .submitButtonShouldBe('disabled');
      FormPage.typeAndBlur('birthDate', '10.10.2000')
        .submitButtonShouldBe('disabled');
      FormPage.typeAndBlur('employmentState', 'B')
        .submitButtonShouldBe('disabled');

      cy.get('mat-option')
        .contains('Bewerber')
        .click();

      FormPage.submitButtonShouldBe('enabled');
    });
  });

  describe('add member form', () => {
    beforeEach(() => {
      FormPage.visitAdd('member');
    });

    it('should be the add member site', () => {
      FormPage.shouldHaveTitle(translations.FORM.ADD.ACTION, 'Member');
    });

    it('should submit member and show him in list', () => {
      cy.intercept('POST', '**/api/v1/members')
        .as('createMember');

      FormPage.submitButtonShouldBe('disabled');
      FormPage.typeAndBlur('firstName', 'John');
      FormPage.typeAndBlur('lastName', 'Doe');
      FormPage.typeAndBlur('abbreviation', 'JD');
      FormPage.typeAndBlur('birthDate', '10.10.2000');
      FormPage.typeAndBlur('dateOfHire', '10.10.2000');
      FormPage.typeAndBlur('employmentState', 'B');

      cy.get('mat-option')
        .contains('Bewerber')
        .click();

      FormPage.typeAndBlur('organisationUnit', '/zh');

      cy.get('mat-option')
        .contains('/zh')
        .click();

      FormPage.save();

      cy.wait('@createMember')
        .its('response.statusCode')
        .should('eq', 201);

      OverviewPage.memberRows()
        .contains('John Doe', { matchCase: false });
    });
  });

  describe('edit member form', () => {
    it('should be the edit member site', () => {
      FormPage.visitEdit(1, 'member');
      FormPage.shouldHaveTitle(translations.FORM.EDIT.ACTION, 'Member');
    });

    it('should get all member data', () => {
      FormPage.visitEdit(1, 'member');
      const expectedMemberData = {
        firstName: 'Lena',
        lastName: 'Müller',
        abbreviation: 'LM',
        birthDate: '10.08.1999',
        dateOfHire: '15.07.2021',
        employmentState: 'Member',
        organisationUnit: '/zh'
      };

      Object.entries(expectedMemberData)
        .forEach(([field,
          value]) => {
          FormPage.shouldHaveFieldValue(field, value);
        });
    });

    it('should save changes to member', () => {
      cy.intercept('PUT', '**/api/v1/members/1')
        .as('updateMember');

      FormPage.visitEdit(1, 'member');

      const changedMemberData = {
        firstName: 'Lena Maria',
        lastName: 'Müller-Keller',
        abbreviation: 'LMK',
        birthDate: '11.08.1999',
        dateOfHire: '16.07.2021'
      };

      Object.entries(changedMemberData)
        .forEach(([field,
          value]) => {
          FormPage.clearAndBlur(field);
          FormPage.typeAndBlur(field, value);
        });

      FormPage.clearAndBlur('employmentState');
      FormPage.typeAndBlur('employmentState', 'Bewerber');
      cy.get('mat-option')
        .contains('Bewerber')
        .click();

      FormPage.type('roles', 'Developer');
      cy.get('mat-option')
        .contains('Developer')
        .click();

      FormPage.clearAndBlur('organisationUnit');
      FormPage.typeAndBlur('organisationUnit', '/zh');
      cy.get('mat-option')
        .contains('/zh')
        .click();

      FormPage.save();

      cy.wait('@updateMember')
        .its('response.statusCode')
        .should('eq', 200);
      cy.location('pathname')
        .should('eq', '/member/1');

      FormPage.visitEdit(1, 'member');

      Object.entries({ ...changedMemberData,
        employmentState: 'Bewerber',
        organisationUnit: '/zh' })
        .forEach(([field,
          value]) => {
          FormPage.shouldHaveFieldValue(field, value);
        });
      cy.get('mat-chip-row')
        .should('contain.text', 'Developer');
    });
  });

  describe('show toaster message member form (parameterised)', () => {
    const cases = [{
      input: 'J',
      field: 'firstName',
      expectedMessage: 'Vorname mit dem Wert "J" muss zwischen 2 und 250 liegen.',
      expectError: true
    },
    {
      input: '12345678901234512345'.repeat(20),
      field: 'firstName',
      expectedMessage: 'Vorname mit dem Wert "123456789012345..." muss zwischen 2 und 250 liegen.',
      expectError: true
    },
    {
      input: 'John',
      field: 'firstName',
      expectedMessage: 'Member wurde erfolgreich aktualisiert.',
      expectError: false
    }];

    cases.forEach(({ input, field, expectedMessage, expectError }) => {
      it(`should show toaster with ${expectedMessage}`, () => {
        FormPage.visitEdit(2, 'member');
        FormPage.clearAndBlur(field);
        FormPage.typeAndBlur(field, input);
        FormPage.save();

        if (expectError) {
          FormPage.shouldShowErrorToast(expectedMessage);
        } else {
          FormPage.shouldShowSuccessToast(expectedMessage);
        }
      });
    });
  });
});
