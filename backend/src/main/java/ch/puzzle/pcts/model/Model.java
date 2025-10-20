package ch.puzzle.pcts.model;

public interface Model {
    Long getId();
    void setId(Long id);
    boolean equals(Object o);
    int hashCode();
}
