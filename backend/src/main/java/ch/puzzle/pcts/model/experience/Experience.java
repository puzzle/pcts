package ch.puzzle.pcts.model.experience;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE experience SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Experience implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String employer;

    private int percent;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_type")
    private ExperienceType type;

    private String comment;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private Experience(Long id, Member member, String name, String employer, int percent, ExperienceType type,
                       String comment, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.member = member;
        this.name = trim(name);
        this.employer = trim(employer);
        this.percent = percent;
        this.type = type;
        this.comment = trim(comment);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Experience() {
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

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = trim(employer);
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public ExperienceType getType() {
        return type;
    }

    public void setType(ExperienceType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Experience that))
            return false;
        return getPercent() == that.getPercent() && Objects.equals(getId(), that.getId())
               && Objects.equals(getMember(), that.getMember()) && Objects.equals(getName(), that.getName())
               && Objects.equals(getEmployer(), that.getEmployer()) && Objects.equals(getType(), that.getType())
               && Objects.equals(getComment(), that.getComment()) && Objects.equals(getStartDate(), that.getStartDate())
               && Objects.equals(getEndDate(), that.getEndDate())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getMember(),
                      getName(),
                      getEmployer(),
                      getPercent(),
                      getType(),
                      getComment(),
                      getStartDate(),
                      getEndDate(),
                      getDeletedAt());
    }

    @Override
    public String toString() {
        return "Experience{" + "id=" + id + ", member=" + member + ", name='" + name + '\'' + ", employer='" + employer
               + '\'' + ", percent=" + percent + ", type=" + type + ", comment='" + comment + '\'' + ", startDate="
               + startDate + ", endDate=" + endDate + ", deletedAt=" + deletedAt + '}';
    }

    public static class Builder {
        private Long id;
        private Member member;
        private String name;
        private String employer;
        private int percent;
        private ExperienceType type;
        private String comment;
        private LocalDate startDate;
        private LocalDate endDate;

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

        public Builder withEmployer(String employer) {
            this.employer = trim(employer);
            return this;
        }

        public Builder withPercent(int percent) {
            this.percent = percent;
            return this;
        }

        public Builder withType(ExperienceType type) {
            this.type = type;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
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

        public Experience build() {
            return new Experience(id, member, name, employer, percent, type, comment, startDate, endDate);
        }
    }
}
