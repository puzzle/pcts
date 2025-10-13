import FormPage from '../pages/formPage'
import translations from '../../public/i18n/de.json';


describe('MemberFormComponent', () => {
  beforeEach(() => {
    FormPage.visitAdd()
  });

  ["name", "lastName", "birthday", "employmentState"].forEach((fieldName) => {
    it(`should show required error for field ${fieldName}`, () => {
      cy.getByTestId(fieldName).focus().blur()

      cy.getByTestId('validation-error').should('include.text', translations.VALIDATION.REQUIRED)
    })
  });

  ["employmentState", "organisationUnit"].forEach((fieldName) => {
    it(`should show invalid entry error for field ${fieldName}`, () => {
      cy.getByTestId(fieldName).type("invalid entry").blur()

      cy.getByTestId('validation-error').should('include.text', translations.VALIDATION.INVALID_ENTRY)
    })
  });

    ["birthday", "dateOfHire"].forEach((fieldName) => {
    it(`should show invalid date error for field ${fieldName}`, () => {
      cy.getByTestId(fieldName).type("invalid entry").blur()

      cy.getByTestId('validation-error').should('include.text', translations.VALIDATION.MATDATEPICKERPARSE)
    })
  })

  it('should activate submit button when everything is filled out', () => {
    FormPage.isSaveDisabled()
    FormPage.addTextToFieldAndCheckButtonState("name", "John")
    FormPage.addTextToFieldAndCheckButtonState("lastName", "Doe")
    FormPage.addTextToFieldAndCheckButtonState("birthday", "1.1.2000")
    FormPage.addTextToFieldAndCheckButtonState("employmentState", "Bewerber")

    cy.get('mat-option').contains('Bewerber').click();


    cy.getByTestId('submit-button').should('be.enabled');
  });
})

describe('Member form', () => {
  beforeEach(() => {
    FormPage.visitAdd()
  })

  it('should be the add member site', () => {
    cy.getByTestId('title').should('have.text','Member '+ translations.GENERAL.ADD)
  });
});

describe('edit member', () => {
  beforeEach(() => {
    FormPage.visitEdit()
  });

  it('should be the edit member site', () => {
    cy.getByTestId('title').should('have.text','Member '+ translations.GENERAL.EDIT)
  });

  it('should get all member data', () => {
    cy.getByTestId('name').should('have.value','Ja')
    cy.getByTestId('lastName').should('have.value','Morant')
    cy.getByTestId('abbreviation').should('have.value','JM')
    cy.getByTestId('birthday').should('have.value',"1.1.2000")
    cy.getByTestId('dateOfHire').should('have.value','1.1.2025')
    cy.getByTestId('employmentState').should('have.value',"Member")
    cy.getByTestId('organisationUnit').should('have.value', '/mem');
  })
});
