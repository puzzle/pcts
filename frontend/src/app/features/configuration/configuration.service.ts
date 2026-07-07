import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConfigurationModel } from './configuration.model';
import { HelpUrlModel } from './HelpUrl.model';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/configuration';

  getConfiguration(): Observable<ConfigurationModel> {
    return this.httpClient.get<ConfigurationModel>(this.API_URL + '/authorization');
  }

  getHelpUrl(): Observable<HelpUrlModel> {
    return this.httpClient.get<HelpUrlModel>(this.API_URL + '/help');
  }
}
