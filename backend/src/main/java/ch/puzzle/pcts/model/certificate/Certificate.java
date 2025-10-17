package ch.puzzle.pcts.model.certificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE certificate SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @Override
    public String toString() {
        return "Certificate{" + "id=" + id + ", name='" + name + '\'' + ", points=" + points + ", comment='" + comment
               + '\'' + ", tags=" + tags + ", certificateType=" + certificateType + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Certificate certificate)) {
            return false;
        }
        return Objects.equals(id, certificate.id) && Objects.equals(name, certificate.name)
               && Objects.equals(points, certificate.points) && Objects.equals(comment, certificate.comment)
               && Objects.equals(tags, certificate.tags) && certificateType == certificate.certificateType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points, comment, tags, certificateType);
    }
}
