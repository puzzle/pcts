package ch.puzzle.pcts.model.memberrole;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE member_role SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class MemberRole implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private MemberRole(Builder builder) {
        this.id = builder.id;
        this.memberId = builder.memberid;
        this.roleId = builder.roleid;
        this.deletedAt = null;
    }

    public MemberRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberRole that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMemberId(), that.getMemberId())
               && Objects.equals(getRoleId(), that.getRoleId()) && Objects.equals(deletedAt, that.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getRoleId());
    }

    @Override
    public String toString() {
        return "MemberRole{" + "id=" + id + ", memberId=" + memberId + ", roleId=" + roleId + ", deletedAt=" + deletedAt
               + '}';
    }

    public static final class Builder {
        private Long id;
        private Long memberid;
        private Long roleid;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMemberId(Long memberid) {
            this.memberid = memberid;
            return this;
        }

        public Builder withRoleId(Long roleid) {
            this.roleid = roleid;
            return this;
        }

        public MemberRole build() {
            return new MemberRole(this);
        }
    }
}
