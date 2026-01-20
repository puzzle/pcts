import { Page } from './page';

class CvMemberPage extends Page {
  cvTable(tableName: string) {
    return cy.getByTestId('cv-table-' + tableName);
  }

  showListButton() {
    return cy.getByTestId('limit-list-button');
  }

  cvTableRows(tableName: string) {
    return this.cvTable(tableName)
      .findByTestId('member-cv-table-row');
  }
}

export default new CvMemberPage();
