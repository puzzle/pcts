import FormPage from '../pages/formPage';
import translations from '../../public/i18n/de.json';
import OverviewPage from '../pages/overviewPage';


describe('MemberFormComponent', () => {
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
    OverviewPage.visit();
    FormPage.visitAdd('member');

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
    FormPage.visitEdit(1, 'member');

    FormPage.clearAndBlur('firstName');
    FormPage.typeAndBlur('firstName', 'Lena');

    FormPage.clearAndBlur('lastName');
    FormPage.typeAndBlur('lastName', 'Müller');

    FormPage.clearAndBlur('abbreviation');
    FormPage.typeAndBlur('abbreviation', 'LM');

    FormPage.clearAndBlur('birthDate');
    FormPage.typeAndBlur('birthDate', '10.08.1999');

    FormPage.clearAndBlur('dateOfHire');
    FormPage.typeAndBlur('dateOfHire', '15.07.2021');

    FormPage.clearAndBlur('employmentState');
    FormPage.typeAndBlur('employmentState', 'Member');
    cy.get('mat-option')
      .contains('Member')
      .click();

    FormPage.clearAndBlur('organisationUnit');
    FormPage.typeAndBlur('organisationUnit', '/zh');
    cy.get('mat-option')
      .contains('/zh')
      .click();

    FormPage.save();
    OverviewPage.visit();

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
    expectedMessage: 'Member wurde erforlgreich aktualisiert.',
    expectError: false
  }];

  cases.forEach(({ input, field, expectedMessage, expectError }) => {
    it(`should show toaster with ${expectedMessage}`, () => {
      cy.on('uncaught:exception', (err, runnable) => {
        return !(err.message.includes('Http failure response') ?? err.message.includes('400'));
      });
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
