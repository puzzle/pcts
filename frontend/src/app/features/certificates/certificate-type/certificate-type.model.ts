import { TagModel } from './tag.model';

export interface CertificateTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  tags: TagModel[];
}
