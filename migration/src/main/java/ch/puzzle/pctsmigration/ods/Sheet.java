package ch.puzzle.pctsmigration.ods;

import java.util.ArrayList;
import java.util.List;

public class Sheet {
    private String name;
    private List<Row> rows;

    public Sheet(String name, List<Row> rows) {
        this.name = name;
        this.rows = rows;
    }

    public Sheet() {
        this.name = "";
        this.rows = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row row) {
        rows.add(row);
    }
}
