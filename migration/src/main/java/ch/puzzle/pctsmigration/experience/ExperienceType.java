package ch.puzzle.pctsmigration.experience;

public enum ExperienceType {
    INTERNSHIP("Praktikum"),
    WORK_EXPERIENCE("Berufserfahrung"),
    SIDE_JOB("Nebenerwerb");

    private final String value;

    ExperienceType(String label) {
        this.value = label;
    }

    public String getValue() {
        return value;
    }
}