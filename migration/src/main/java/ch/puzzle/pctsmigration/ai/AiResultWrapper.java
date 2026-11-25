package ch.puzzle.pctsmigration.ai;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public abstract class AiResultWrapper<T> {

    @Valid
    @NotNull
    private List<T> items;

    public AiResultWrapper(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
}