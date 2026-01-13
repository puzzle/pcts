package ch.puzzle.pcts.model.memberoverview;

import java.io.Serializable;
import java.util.Objects;

public class MemberOverviewId implements Serializable {

    private Long memberId;
    private Long certificateId;
    private Long degreeId;
    private Long experienceId;

    public MemberOverviewId() {
    }

    public MemberOverviewId(Long memberId, Long certificateId, Long degreeId, Long experienceId) {
        this.memberId = memberId;
        this.certificateId = certificateId;
        this.degreeId = degreeId;
        this.experienceId = experienceId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberOverviewId that)) {
            return false;
        }
        return Objects.equals(getMemberId(), that.getMemberId())
               && Objects.equals(getCertificateId(), that.getCertificateId())
               && Objects.equals(getDegreeId(), that.getDegreeId())
               && Objects.equals(getExperienceId(), that.getExperienceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getCertificateId(), getDegreeId(), getExperienceId());
    }

    @Override
    public String toString() {
        return "MemberOverviewId{" + "memberId=" + memberId + ", certificateId=" + certificateId + ", degreeId="
               + degreeId + ", experienceId=" + experienceId + '}';
    }
}
