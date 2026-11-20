import { RelevancyModel } from '../generic-radio-form-table/RelevancyModel';
import { ValidationError } from '@angular/forms/signals';

export interface NoRelevancyError<T extends RelevancyModel> extends ValidationError {
  kind: 'noRelevancy';
  row: T;
}
