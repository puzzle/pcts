package ch.puzzle.pcts.model.certificate;

public enum CertificateType {
    CERTIFICATE(false),
    MILITARY_FUNCTION(true),
    YOUTH_AND_SPORT(true),
    LEADERSHIP_TRAINING(true);

    private final boolean isLeadershipExperience;

    CertificateType(boolean isLeadershipExperience) {
        this.isLeadershipExperience = isLeadershipExperience;
    }

    public boolean isLeadershipExperience() {
        return isLeadershipExperience;
    }
}
