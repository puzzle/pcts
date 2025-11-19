package ch.puzzle.pcts.model.certificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE certificate SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Certificate implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_type_id")
    private CertificateType certificateType;

    @NotNull(message = "{attribute.not.null}")
    private LocalDate completedAt;

    private LocalDate validUntil;

    private String comment;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    private Certificate(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.certificateType = builder.certificateType;
        this.completedAt = builder.completedAt;
        this.validUntil = builder.validUntil;
        this.comment = trim(builder.comment);
        this.deletedAt = null;
    }

    public Certificate() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Certificate that))
            return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getCertificateType(), that.getCertificateType())
               && Objects.equals(getCompletedAt(), that.getCompletedAt())
               && Objects.equals(getValidUntil(), that.getValidUntil())
               && Objects.equals(getComment(), that.getComment())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getMember(),
                      getCertificateType(),
                      getCompletedAt(),
                      getValidUntil(),
                      getComment(),
                      getDeletedAt());
    }

    @Override
    public String toString() {
        return "MemberCertificates{" + "id=" + id + ", member=" + member + ", certificateType=" + certificateType
               + ", completedAt=" + completedAt + ", validUntil=" + validUntil + ", comment='" + comment + '\''
               + ", deletedAt=" + deletedAt + '}';
    }

    public static class Builder {
        private Long id;
        private Member member;
        private CertificateType certificateType;
        private LocalDate completedAt;
        private LocalDate validUntil;
        private String comment;

        private Builder() {

        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMember(Member member) {
            this.member = member;
            return this;
        }

        public Builder withCertificateType(CertificateType certificateType) {
            this.certificateType = certificateType;
            return this;
        }

        public Builder withCompletedAt(LocalDate completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder withValidUntil(LocalDate validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public Certificate build() {
            return new Certificate(this);
        }
    }
}
