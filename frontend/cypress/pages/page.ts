export abstract class Page {
  visitDefaultPage() {
    cy.visit('/');
  }
}
