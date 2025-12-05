import { CertificateKind } from '../../certificates/certificate-type/certificate-kind.enum';

export interface LeadershipExperienceTypeModel {
  id: number;
  name: string;
  points: number;
  comment: string | null;
  certificateKind: CertificateKind;
}
