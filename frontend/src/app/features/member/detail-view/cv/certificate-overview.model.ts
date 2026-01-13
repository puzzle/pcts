export interface CertificateOverviewModel {
  id: number;
  certificateTypeName: string;
  completedAt: Date;
  comment: string | null;
}
