describe('example page', () => {
  beforeEach(() => {
    cy.visitDefaultPage();
  });

  it('should show html', () => {
    cy.get('html')
      .should('be.visible');
  });
});
