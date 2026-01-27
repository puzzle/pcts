// Those tests will not succeed locally when you run them without using docker, because the csp is set by the nginx.
describe('Security', () => {
  it('should have set the nonce', () => {
    cy.request({
      url: '/',
      method: 'GET'
    })
      .then((response) => {
        const csp = response.headers['content-security-policy'];

        expect(csp).to.not.include('script-src \'nonce-$request_id\'');
        expect(csp).to.not.include('style-src \'self\' \'sha256-mwH/Oz1bMiZ9vHH84YJ6PbP6BpLW5nG9AD9Lad8u1+c=\' \'nonce-$request_id\';');
      });
  });

  it('should have a csp header', () => {
    cy.request({
      url: '/',
      method: 'GET'
    })
      .then((response) => {
        expect(response.headers).to.have.property('content-security-policy');
      });
  });
});
