package ch.puzzle.pcts.model.certificatetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.PCTSPointsValidation;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@SQLDelete(sql = "UPDATE certificate_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class CertificateType implements Model {
    private static final Logger log = LoggerFactory.getLogger(CertificateType.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    @PCTSPointsValidation
    private BigDecimal points;

    private String comment;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = "certificate_type_tag", joinColumns = @JoinColumn(name = "certificate_type_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private CertificateKind certificateKind;

    @NotNull(message = "{attribute.not.null}")
    @PositiveOrZero(message = "{attribute.not.negative}")
    private double effort;

    private Integer examDuration;

    private String link;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private ExamType examType;

    @PCTSStringValidation
    private String publisher;

    private int linkErrorCount = 0;

    private LocalDateTime linkLastCheckedAt = null;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private CertificateType(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
        this.points = builder.points;
        this.comment = trim(builder.comment);
        this.tags = builder.tags;
        this.certificateKind = builder.certificateKind;
        this.effort = builder.effort;
        this.examDuration = builder.examDuration;
        this.link = builder.link;
        this.examType = builder.examType;
        this.publisher = builder.publisher;
    }

    public CertificateType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = trim(name);
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
        this.comment = trim(comment);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public CertificateKind getCertificateKind() {
        return certificateKind;
    }

    public void setCertificateKind(CertificateKind certificateKind) {
        this.certificateKind = certificateKind;
    }

    public double getEffort() {
        return effort;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }

    public Integer getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(Integer examDuration) {
        this.examDuration = examDuration;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getLinkErrorCount() {
        return linkErrorCount;
    }

    public LocalDateTime getLinkLastCheckedAt() {
        return linkLastCheckedAt;
    }

    public void recordLinkFailure() {
        this.linkErrorCount++;
        this.linkLastCheckedAt = LocalDateTime.now();
    }

    public void resetLinkStatus() {
        this.linkErrorCount = 0;
        this.linkLastCheckedAt = LocalDateTime.now();
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CertificateType that))
            return false;
        return Double.compare(getEffort(), that.getEffort()) == 0 && getLinkErrorCount() == that.getLinkErrorCount()
               && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
               && Objects.equals(getPoints(), that.getPoints()) && Objects.equals(getComment(), that.getComment())
               && Objects.equals(getTags(), that.getTags()) && getCertificateKind() == that.getCertificateKind()
               && Objects.equals(getExamDuration(), that.getExamDuration()) && Objects.equals(getLink(), that.getLink())
               && Objects.equals(getExamType(), that.getExamType())
               && Objects.equals(getPublisher(), that.getPublisher())
               && Objects.equals(getLinkLastCheckedAt(), that.getLinkLastCheckedAt())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getName(),
                      getPoints(),
                      getComment(),
                      getTags(),
                      getCertificateKind(),
                      getEffort(),
                      getExamDuration(),
                      getLink(),
                      getExamType(),
                      getPublisher(),
                      getLinkErrorCount(),
                      getLinkLastCheckedAt(),
                      getDeletedAt());
    }

    @Override
    public String toString() {
        return "CertificateType{" + "id=" + getId() + ", name='" + getName() + '\'' + ", points=" + getPoints()
               + ", comment='" + getComment() + '\'' + ", tags=" + getTags() + ", certificateKind="
               + getCertificateKind() + ", effort=" + getEffort() + ", duration=" + getExamDuration() + ", link='"
               + getLink() + '\'' + ", examType='" + getExamType() + '\'' + ", publisher='" + getPublisher() + '\''
               + ", linkErrorCount=" + getLinkErrorCount() + ", linkLastCheckedAt=" + getLinkLastCheckedAt()
               + ", deletedAt=" + getDeletedAt() + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private BigDecimal points;
        private String comment;
        private Set<Tag> tags;
        private CertificateKind certificateKind;
        private double effort;
        private Integer examDuration;
        private String link;
        private ExamType examType;
        private String publisher;

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

        public Builder withTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withCertificateKind(CertificateKind certificateKind) {
            this.certificateKind = certificateKind;
            return this;
        }

        public Builder withEffort(double effort) {
            this.effort = effort;
            return this;
        }

        public Builder withExamDuration(Integer examDuration) {
            this.examDuration = examDuration;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withExamType(ExamType examType) {
            this.examType = examType;
            return this;
        }

        public Builder withPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public CertificateType build() {
            return new CertificateType(this);
        }
    }
}
