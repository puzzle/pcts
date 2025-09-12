describe('example page', () => {
  it('should show header', () => {
    cy.get('header')
      .should('be.visible');
  });
});
