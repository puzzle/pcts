import { MemberModel } from '../member/member.model';
import { CertificateTypeModel } from './certificate-type/certificate-type.model';

export interface CertificateModel {
  id: number;
  member: MemberModel;
  certificateType: CertificateTypeModel;
  completedAt: Date;
  validUntil: Date | null;
  comment: string | null;
}
