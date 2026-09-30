import { Page } from './page';

class CertificateTypeOverviewPage extends Page {
  visit() {
    cy.visit('/certificate');
  }

  certificateButton() {
    return cy.getByTestId('certificate-overview')
      .click();
  }

  certificateRows() {
    return cy.getByTestId('expand-row')
      .first()
      .click();
  }

  certificateDetailView() {
    return cy.get('.detail-content')
      .first();
  }
}
export default new CertificateTypeOverviewPage();


