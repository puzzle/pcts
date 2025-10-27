import { Page } from './page';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';

class HomePage extends Page {
  visit() {
    cy.visit('/');
  }

  title() {
    return cy.getByTestId('member-title');
  }

  fillSearchInput(text: string) {
    cy
      .getByTestId('search-input')
      .clear()
      .type(text);
  }

  memberTable() {
    return cy.getByTestId('member-table');
  }

  memberRows() {
    return cy.getByTestId('member-row');
  }

  noResultsRow() {
    return cy.getByTestId('no-results-row');
  }

  resetEmploymentStateFilter() {
    return cy.getByTestId('all');
  }

  filterByState(state: EmploymentState) {
    return cy.getByTestId(state);
  }

  createMemberButton() {
    return cy.getByTestId('add-member-button');
  }
}

export default new HomePage();
