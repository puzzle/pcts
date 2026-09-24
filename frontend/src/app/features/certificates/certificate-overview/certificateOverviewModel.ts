import { TagModel } from '../certificate-type/tag.model';

export interface CertificateOverviewModel {
  id: number;
  name: string;
  publisher: string;
  points: number;
  tags: TagModel[];
}
