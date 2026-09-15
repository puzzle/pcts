import * as users from '../fixtures/users.json';
import memberDetailPage from '../pages/memberDetailPage';
import modalPage from '../pages/modalPage';
import formPage from '../pages/formPage';

describe('Edit degree modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  const openDegreeModal = () => {
    cy.getByTestId('generic-table-cell')
      .eq(1)
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
      .should('include.text', 'Ausbildung bearbeiten');
  });

  it('should save changes correctly', () => {
    openDegreeModal();

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
  });
});
