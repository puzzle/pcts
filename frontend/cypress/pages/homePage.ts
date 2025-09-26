import { Page } from './page';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';

class HomePage extends Page {
  visit() {
    cy.visit('/');
  }

  title() {
    return cy.getByTestId('member-title');
  }

  searchInput() {
    return cy.getByTestId('search-input');
  }

  fillSearchInput(text: string) {
    this.searchInput()
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

  filterAll() {
    return cy.getByTestId('all');
  }

  filterByState(state: EmploymentState) {
    return cy.getByTestId(state);
  }
}

export default new HomePage();
