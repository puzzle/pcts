import { Page } from './page';

class CertificateTypeOverviewPage extends Page {
  visit() {
    cy.visitApp('/certificate');
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

  tagFilter() {
    cy.getByTestId('tag-field')
      .click();
  }

  selectOptionFromTagFilter(value: string) {
    cy.get('mat-option')
      .contains(value)
      .click();
  }

  checkIfOptionFromTagFilterIsSelected(value: string) {
    cy.contains('mat-option', value)
      .find('mat-pseudo-checkbox')
      .should('have.class', 'mat-pseudo-checkbox-checked');
  }

  checkIfOptionFromTagFilterIsNotSelected(value: string) {
    cy.contains('mat-option', value)
      .should('be.visible')
      .find('mat-pseudo-checkbox')
      .should('not.have.class', 'mat-pseudo-checkbox-checked');
  }
}
export default new CertificateTypeOverviewPage();


