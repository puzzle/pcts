package ch.puzzle.pcts.model.degree;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE degree SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Degree implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    @NotNull
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String institution;

    @NotNull
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private DegreeType type;

    @NotNull
    private Date startDate;

    private Date endDate;

    private String comment;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    private Degree(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.name = builder.name;
        this.institution = builder.institution;
        this.completed = builder.completed;
        this.type = builder.type;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.comment = builder.comment;
        this.deletedAt = null;
    }

    public Degree() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public DegreeType getType() {
        return type;
    }

    public void setType(DegreeType type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Degree degree = (Degree) o;
        return Objects.equals(getId(), degree.getId()) && Objects.equals(getMember(), degree.getMember())
               && Objects.equals(getName(), degree.getName())
               && Objects.equals(getInstitution(), degree.getInstitution())
               && Objects.equals(getCompleted(), degree.getCompleted()) && Objects.equals(getType(), degree.getType())
               && Objects.equals(getStartDate(), degree.getStartDate())
               && Objects.equals(getEndDate(), degree.getEndDate())
               && Objects.equals(getDeletedAt(), degree.getDeletedAt())
               && Objects.equals(getComment(), degree.getComment());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getMember(),
                      getName(),
                      getInstitution(),
                      getCompleted(),
                      getType(),
                      getStartDate(),
                      getEndDate(),
                      getDeletedAt(),
                      getComment());
    }

    @Override
    public String toString() {
        return "Degree{" + "id=" + id + ", member=" + member + ", name='" + name + '\'' + ", institution='"
               + institution + '\'' + ", completed=" + completed + ", type=" + type + ", startDate=" + startDate
               + ", endDate=" + endDate + ", deletedAt=" + deletedAt + ", comment='" + comment + '\'' + '}';
    }

    public static final class Builder {
        private Long id;
        private Member member;
        private String name;
        private String institution;
        private Boolean completed;
        private DegreeType type;
        private Date startDate;
        private Date endDate;
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

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withInstitution(String institution) {
            this.institution = institution;
            return this;
        }

        public Builder withCompleted(Boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder withType(DegreeType type) {
            this.type = type;
            return this;
        }

        public Builder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Degree build() {
            return new Degree(this);
        }
    }

}
