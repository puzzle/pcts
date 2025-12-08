package ch.puzzle.pcts.model.memberoverview;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_overview_view")
public class MemberOverview implements Model {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(columnDefinition = "jsonb")
    private String overview;

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }
}
