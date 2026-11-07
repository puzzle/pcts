# Security Headers

We use nginx to configure the header `Content-Security-Policy` and managing the nonce flow.

## Nonce flow

- **Injection**: A placeholder string is set within the Angular `index.html` file. At runtime nginx overwrites this placeholder with a unique request ID.
- **Header Configuration**: Nginx sets the `script-src` and `style-src` directive in the `Content-Security-Policy` header to `nonce-${request_id}`.
- **Inline Styles**: Angular's Critical CSS Inlining feature (used to improve initial load speed)
  has been disabled. This is because every inline style requires the nonce, which cannot easily be applied to Angular's generated styles.
