package ch.puzzle.pcts.model.example;

import jakarta.persistence.*;

@Entity
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_example")
    @SequenceGenerator(name = "sequence_example", allocationSize = 1)
    private long id;

    private String text;

    public Example(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Example() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
