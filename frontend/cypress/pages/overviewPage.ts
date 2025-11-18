import { Page } from './page';
import { EmploymentState } from '../../src/app/shared/enum/employment-state.enum';

class OverviewPage extends Page {
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

  firstMemberRows() {
    return cy.getByTestId('member-row')
      .first();
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

  getMember(firstName: string, lastName: string) {
    return cy.getByTestId('member-row')
      .filter((_, row) => {
        const text = row.innerText;
        return text.includes(firstName) && text.includes(lastName);
      });
  }

  getMemberByFullName(firstName: string, lastName: string) {
    return this.memberTable()
      .findByTestId('member-row', firstName + ' ' + lastName);
  }

  sortAttribute(sortAttribute: string) {
    return cy.getByTestId(sortAttribute + '-sort')
      .click();
  }
}

export default new OverviewPage();
