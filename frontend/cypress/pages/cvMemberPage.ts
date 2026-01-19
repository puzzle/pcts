import { Page } from './page';

class CvMemberPage extends Page {
  cvTable(tableName: string) {
    return cy.getByTestId('cv-table-' + tableName);
  }

  showListButton() {
    return cy.getByTestId('show-list');
  }
}

export default new CvMemberPage();
