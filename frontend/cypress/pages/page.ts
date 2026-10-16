export abstract class Page {
  visitDefaultPage() {
    cy.visitApp('/');
  }
}
