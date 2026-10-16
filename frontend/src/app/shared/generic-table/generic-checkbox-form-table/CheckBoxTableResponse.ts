import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface CheckBoxTableResponse<T> {
  row: T;
  relevancy: Relevancy;
  amount?: number | null;
}
