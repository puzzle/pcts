import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface RelevancyWeightsRow {
  id: number;
  relevancies: Record<Relevancy, { weight: number;
    checked: boolean; }>;
}
