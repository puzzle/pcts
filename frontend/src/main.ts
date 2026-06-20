import { bootstrapApplication } from '@angular/platform-browser';
import { buildAppConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { RuntimeConfig } from './app/core/runtime-config.model';

// This should be fine, as fetch is always available through the browser
// eslint-disable-next-line no-undef
fetch('/config.json')
  .then((res) => res.json())
  .then((config: RuntimeConfig) => bootstrapApplication(AppComponent, buildAppConfig(config)))
  .catch((err) => console.error(err));
