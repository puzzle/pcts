import * as users from '../fixtures/users.json';

// These tests rely on Nginx to set headers, so they may fail without Docker/Proxy.
describe('Security: Content Security Policy', () => {
  let csp: string;

  const expectedDirectives = [
    { name: 'default-src',
      value: 'self' },
    { name: 'object-src',
      value: 'none' },
    { name: 'base-uri',
      value: 'self' },
    { name: 'connect-src',
      value: 'self' },
    { name: 'font-src',
      value: 'self' },
    { name: 'frame-src',
      value: 'self' },
    { name: 'img-src',
      value: 'self' },
    { name: 'manifest-src',
      value: 'self' },
    { name: 'media-src',
      value: 'self' },
    { name: 'worker-src',
      value: 'none' }
  ];

  before(() => {
    cy.loginAsUser(users.gl);

    cy.request({
      url: '/',
      method: 'GET'
    })
      .then((response) => {
        expect(response.headers).to.have.property('content-security-policy');
        csp = response.headers['content-security-policy'] as string;
      });
  });

  it('should contain all required directives with correct values', () => {
    expectedDirectives.forEach(({ name, value }) => {
      expect(csp, `Checking directive: ${name}`).to.include(`${name} '${value}'`);
    });
  });

  it('should have replaced the Nginx request_id variable with a real nonce', () => {
    expect(csp).to.not.include('nonce-$request_id');

    const nonceRegex = /'nonce-[A-Za-z0-9+/=]+'/;
    expect(csp).to.match(nonceRegex, 'CSP should contain a valid generated nonce');
  });

  it('should include specific style-src hashes', () => {
    const expectedHash = '\'sha256-mwH/Oz1bMiZ9vHH84YJ6PbP6BpLW5nG9AD9Lad8u1+c=\'';
    expect(csp).to.include(expectedHash);
  });
});
