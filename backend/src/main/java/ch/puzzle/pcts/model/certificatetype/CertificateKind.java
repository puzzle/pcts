package ch.puzzle.pcts.model.certificatetype;

public enum CertificateKind {
    CERTIFICATE(false),
    MILITARY_FUNCTION(true),
    YOUTH_AND_SPORT(true),
    LEADERSHIP_TRAINING(true);

    private final boolean isLeadershipExperienceType;

    CertificateKind(boolean isLeadershipExperience) {
        this.isLeadershipExperienceType = isLeadershipExperience;
    }

    public boolean isLeadershipExperienceType() {
        return isLeadershipExperienceType;
    }
}
