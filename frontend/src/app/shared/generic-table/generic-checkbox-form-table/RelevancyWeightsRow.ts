import { Relevancy } from '../../../features/calculations/relevancy.enum';

export interface RelevancyWeightsRow {
  relevancies: Record<Relevancy, number>;
}
