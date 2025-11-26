import { MemberModel } from '../member/member.model';
import { CertificateTypeModel } from './certificate-type/certificate-type.model';
import { DateTime } from 'luxon';

export interface CertificateModel {
  id: number;
  member: MemberModel;
  certificateType: CertificateTypeModel;
  completedAt: DateTime;
  validUntil: DateTime | null;
  comment: string | null;
}
