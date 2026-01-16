import { Page } from './page';

class CvMemberPage extends Page {
  cvTable(tableName: string) {
    return cy.getByTestId('cv-table-' + tableName);
  }
}

export default new CvMemberPage();
