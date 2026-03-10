package ch.puzzle.pcts.model.leadershipexperience;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.model.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE leadership_experience SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class LeadershipExperience implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leadership_experience_type_id")
    private LeadershipExperienceType leadershipExperienceType;

    private String comment;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    private LeadershipExperience(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.leadershipExperienceType = builder.leadershipExperienceType;
        this.comment = trim(builder.comment);
    }

    public LeadershipExperience() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LeadershipExperienceType getLeadershipExperienceType() {
        return leadershipExperienceType;
    }

    public void setLeadershipExperienceType(LeadershipExperienceType leadershipExperienceType) {
        this.leadershipExperienceType = leadershipExperienceType;
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
        if (!(o instanceof LeadershipExperience that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getLeadershipExperienceType(), that.getLeadershipExperienceType())
               && Objects.equals(getComment(), that.getComment())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMember(), getLeadershipExperienceType(), getComment(), getDeletedAt());
    }

    @Override
    public String toString() {
        return "LeadershipExperience{" + "id=" + getId() + ", member=" + getMember() + ", leadershipExperienceType="
               + getLeadershipExperienceType() + ", comment='" + getComment() + '\'' + ", deletedAt=" + getDeletedAt()
               + '}';
    }

    public static final class Builder {
        private Long id;
        private Member member;
        private LeadershipExperienceType leadershipExperienceType;
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

        public Builder withLeadershipExperienceType(LeadershipExperienceType leadershipExperienceType) {
            this.leadershipExperienceType = leadershipExperienceType;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public LeadershipExperience build() {
            return new LeadershipExperience(this);
        }
    }
}
