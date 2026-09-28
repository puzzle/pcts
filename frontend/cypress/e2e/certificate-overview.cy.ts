import * as users from '../fixtures/users.json';

describe('Certificate modal', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });
});
