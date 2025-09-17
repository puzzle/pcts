package ch.puzzle.pcts.model.certificate;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private BigDecimal points;

    @Column(nullable = false)
    private boolean is_deleted;

    private String comment;

    public Certificate(Long id, String name, BigDecimal points, boolean is_deleted, String comment) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.is_deleted = is_deleted;
        this.comment = comment;
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
        this.name = name;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public boolean isDeleted() {
        return is_deleted;
    }

    public void setDeleted(boolean deleted) {
        is_deleted = deleted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
