package ch.puzzle.pcts.model.leadershipexperiencetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.util.PCTSPointsValidation;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE leadership_experience_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class LeadershipExperienceType implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    @PCTSPointsValidation
    private BigDecimal points;

    private String comment;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private CertificateKind certificateKind;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private LeadershipExperienceType(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
        this.points = builder.points;
        this.comment = trim(builder.comment);
        this.certificateKind = builder.certificateKind;
    }

    public LeadershipExperienceType() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CertificateKind getCertificateKind() {
        return certificateKind;
    }

    public void setCertificateKind(CertificateKind certificateKind) {
        this.certificateKind = certificateKind;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeadershipExperienceType that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
               && Objects.equals(getPoints(), that.getPoints()) && Objects.equals(getComment(), that.getComment())
               && getCertificateKind() == that.getCertificateKind()
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPoints(), getComment(), getCertificateKind(), getDeletedAt());
    }

    public static final class Builder {
        private Long id;
        private String name;
        private BigDecimal points;
        private String comment;
        private CertificateKind certificateKind;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = trim(name);
            return this;
        }

        public Builder withPoints(BigDecimal points) {
            this.points = points;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public Builder withCertificateKind(CertificateKind certificateKind) {
            this.certificateKind = certificateKind;
            return this;
        }

        public LeadershipExperienceType build() {
            return new LeadershipExperienceType(this);
        }
    }
}
