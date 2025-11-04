package ch.puzzle.pcts.model.membercertificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE member_certificate SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class MemberCertificate implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate")
    private Certificate certificate;

    @NotNull(message = "{attribute.not.null}")
    @Past(message = "{attribute.date.past}")
    private Date completed_at;

    private Date valid_until;

    private String comment;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    private MemberCertificate(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.certificate = builder.certificate;
        this.completed_at = builder.completed_at;
        this.valid_until = builder.valid_until;
        this.comment = trim(builder.comment);
        this.deletedAt = null;
    }

    public MemberCertificate() {
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

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Date getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(Date completed_at) {
        this.completed_at = completed_at;
    }

    public Date getValid_until() {
        return valid_until;
    }

    public void setValid_until(Date valid_until) {
        this.valid_until = valid_until;
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
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        MemberCertificate that = (MemberCertificate) object;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getCertificate(), that.getCertificate())
               && Objects.equals(getCompleted_at(), that.getCompleted_at())
               && Objects.equals(getValid_until(), that.getValid_until())
               && Objects.equals(getComment(), that.getComment())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getMember(),
                      getCertificate(),
                      getCompleted_at(),
                      getValid_until(),
                      getComment(),
                      getDeletedAt());
    }

    @Override
    public String toString() {
        return "MemberCertificates{" + "id=" + id + ", member=" + member + ", certificate=" + certificate
               + ", completed_at=" + completed_at + ", valid_until=" + valid_until + ", comment='" + comment + '\''
               + ", deletedAt=" + deletedAt + '}';
    }

    public static class Builder {
        private Long id;
        private Member member;
        private Certificate certificate;
        private Date completed_at;
        private Date valid_until;
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

        public Builder withCertificate(Certificate certificate) {
            this.certificate = certificate;
            return this;
        }

        public Builder withCompleted_at(Date completed_at) {
            this.completed_at = completed_at;
            return this;
        }

        public Builder withValid_until(Date valid_until) {
            this.valid_until = valid_until;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public MemberCertificate build() {
            return new MemberCertificate(this);
        }
    }
}
