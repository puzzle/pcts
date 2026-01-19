import { Page } from './page';

class CvMemberPage extends Page {
  cvTable(tableName: string) {
    return cy.getByTestId('cv-table-' + tableName);
  }

  showListButton() {
    return cy.getByTestId('show-list');
  }

  cvTableRow() {
    return this.cvTable('experience')
      .findByTestId('member-cv-table-row');
  }
}

export default new CvMemberPage();
