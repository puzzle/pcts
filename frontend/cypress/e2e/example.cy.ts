import HomePage from '../pages/homePage';

describe('example page', () => {
  beforeEach(() => {
    HomePage.visitDefaultPage();
  });

  it('should show html', () => {
    cy.get('html')
      .should('be.visible');
  });
});
