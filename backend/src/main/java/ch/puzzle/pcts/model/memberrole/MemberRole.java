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

    public MemberRole(Long id, Long memberId, Long roleId) {
        this.id = id;
        this.memberId = memberId;
        this.roleId = roleId;
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
        if (o == null || getClass() != o.getClass())
            return false;
        MemberRole that = (MemberRole) o;
        return Objects.equals(getMemberId(), that.getMemberId()) && Objects.equals(getRoleId(), that.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getRoleId());
    }
}
