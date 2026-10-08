import { Page } from './page';

class CertificateTypeOverviewPage extends Page {
  visit() {
    cy.visit('/certificate');
  }

  visitViaButton() {
    cy.getByTestId('certificate-overview')
      .click();
    return this;
  }

  table() {
    return cy.getByTestId('generic-table');
  }

  certificateRows() {
    return cy.getByTestId('expand-row');
  }

  firstCertificateRow() {
    return this.certificateRows()
      .first();
  }

  certificateDetailView() {
    return cy.get('.detail-content')
      .first();
  }

  tableRows() {
    return cy.getByTestId('generic-table-row');
  }
}
export default new CertificateTypeOverviewPage();


