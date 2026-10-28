import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    testIsolation: true,
    viewportWidth: 1920,
    viewportHeight: 1080,
    defaultBrowser: "firefox"
  },
  expose: {
    LOGIN_URL: 'http://localhost:8544'
  },
  retries: {
    // Configure retry attempts for `cypress run`
    runMode: 2,
    // Configure retry attempts for `cypress open`
    openMode: 0,
  },
});
