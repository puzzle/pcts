import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface RadioFormTableResponse<T extends object> {
  row: T;
  relevancy: Relevancy;
}
