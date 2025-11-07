package ch.puzzle.pcts.model.degree;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE degree SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Degree implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull(message = "{attribute.not.null}")
    private Member member;

    @PCTSStringValidation
    private String name;

    @PCTSStringValidation
    private String institution;

    @NotNull(message = "{attribute.not.null}")
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    @NotNull(message = "{attribute.not.null}")
    private DegreeType type;

    @NotNull(message = "{attribute.not.null}")
    @PastOrPresent(message = "{attribute.date.past.present}")
    private LocalDate startDate;

    private LocalDate endDate;

    private String comment;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private Degree(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.name = trim(builder.name);
        this.institution = trim(builder.institution);
        this.completed = builder.completed;
        this.type = builder.type;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.comment = trim(builder.comment);
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
        this.name = trim(name);
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = trim(institution);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Degree degree))
            return false;
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
        private LocalDate startDate;
        private LocalDate endDate;
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
            this.name = trim(name);
            return this;
        }

        public Builder withInstitution(String institution) {
            this.institution = trim(institution);
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

        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public Degree build() {
            return new Degree(this);
        }
    }

}
