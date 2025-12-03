import { CertificateKind } from '../../certificates/certificate-type/certificate-kind.enum';

export interface LeadershipExperiencesTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string;
  experienceKind: CertificateKind;
}
