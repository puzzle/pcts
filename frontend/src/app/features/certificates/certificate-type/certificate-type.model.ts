import { TagModel } from './tag.model';
import { CertificateKind } from './certificate-kind.enum';

export interface CertificateTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  tags: TagModel[];
  certificateKind: CertificateKind;
}
