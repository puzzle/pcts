export enum CertificateKind {
  CERTIFICATE = 'CERTIFICATE',
  MILITARY_FUNCTION = 'MILITARY_FUNCTION',
  YOUTH_AND_SPORT = 'YOUTH_AND_SPORT',
  LEADERSHIP_TRAINING = 'LEADERSHIP_TRAINING'
}

const leadershipExperienceTypes: Record<CertificateKind, boolean> = {
  [CertificateKind.CERTIFICATE]: false,
  [CertificateKind.MILITARY_FUNCTION]: true,
  [CertificateKind.YOUTH_AND_SPORT]: true,
  [CertificateKind.LEADERSHIP_TRAINING]: true
};

export function isLeadershipExperienceType(kind: CertificateKind): boolean {
  return leadershipExperienceTypes[kind];
}
