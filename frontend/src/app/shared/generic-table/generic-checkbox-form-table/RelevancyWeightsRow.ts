import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface RelevancyWeightsRow {
  id: number | null;
  relevancies: Record<Relevancy, { weight: number;
    checked: boolean; }>;
}
