package ch.puzzle.pcts.model.certificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE certificate SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Certificate implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal points;

    private String comment;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = "certificate_tag", joinColumns = @JoinColumn(name = "certificate_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CertificateType certificateType;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    public Certificate(Long id, String name, BigDecimal points, String comment, Set<Tag> tags,
                       CertificateType certificateType) {
        this.id = id;
        this.name = trim(name);
        this.points = points;
        this.comment = trim(comment);
        this.tags = tags;
        this.certificateType = certificateType;
    }

    public Certificate(Long id, String name, BigDecimal points, String comment, Set<Tag> tags) {
        this(id, name, points, comment, tags, CertificateType.CERTIFICATE);
    }

    public Certificate(Long id, String name, BigDecimal points, String comment, CertificateType certificateType) {
        this(id, name, points, comment, null, certificateType);
    }

    public Certificate() {
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

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(points, that.points)
               && Objects.equals(comment, that.comment) && Objects.equals(tags, that.tags)
               && certificateType == that.certificateType && Objects.equals(deletedAt, that.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points, comment, tags, certificateType, deletedAt);
    }

    @Override
    public String toString() {
        return "Certificate{" + "id=" + id + ", name='" + name + '\'' + ", points=" + points + ", comment='" + comment
               + '\'' + ", tags=" + tags + ", certificateType=" + certificateType + ", deletedAt=" + deletedAt + '}';
    }
}
