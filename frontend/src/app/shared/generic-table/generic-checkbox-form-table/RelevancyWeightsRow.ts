import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface RelevancyType { weight: number;
  checked: boolean; }

export interface RelevancyWeightsRow {
  id: number | null;
  comment: string;
  relevancies: Record<Relevancy, RelevancyType>;
}
