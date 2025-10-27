import FormPage from '../pages/formPage';
import translations from '../../public/i18n/de.json';
import HomePage from '../pages/homePage';


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
    FormPage.shouldHaveTitle(translations.GENERAL.ADD, 'Member');
  });

  it('should submit member and show him in list', () => {
    HomePage.visit();
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

    HomePage.memberRows()
      .contains('John Doe', { matchCase: false });
  });
});

describe('edit member form', () => {
  it('should be the edit member site', () => {
    FormPage.visitEdit(1, 'member');
    FormPage.shouldHaveTitle(translations.GENERAL.EDIT, 'Member');
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
    FormPage.visitEdit(2, 'member');
    FormPage.clearAndBlur('firstName');
    FormPage.typeAndBlur('firstName', 'Leon');

    FormPage.save();

    HomePage.memberRows()
      .contains('Leon Schmidt', { matchCase: false });
  });
});
