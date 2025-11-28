export interface CertificateDto {
  memberId: number;
  certificateTypeId: number;
  completedAt: string | null;
  validUntil: string | null;
  comment: string | null;
}
