export interface CertificateDto {
  memberId: number;
  certificateTypeId: number;
  completedAt: Date;
  validUntil: Date | null;
  comment: string | null;
}
