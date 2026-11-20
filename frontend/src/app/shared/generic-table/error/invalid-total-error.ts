import { RelevancyWeightsRow } from '../generic-checkbox-form-table/RelevancyWeightsRow';
import { ValidationError } from '@angular/forms/signals';

export interface InvalidTotalError<T extends RelevancyWeightsRow> extends ValidationError { kind: 'invalidTotal';
  row: T;
  total: number; }
