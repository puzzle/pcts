import { bootstrapApplication } from '@angular/platform-browser';
import { buildAppConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { ConfigurationModel } from './app/features/configuration/configuration.model';

// This should be fine, as fetch is always available through the browser
// eslint-disable-next-line no-undef
fetch('/api/v1/configuration')
  .then((res) => res.json())
  .then((config: ConfigurationModel) => bootstrapApplication(AppComponent, buildAppConfig(config)))
  .catch((err) => console.error(err));
