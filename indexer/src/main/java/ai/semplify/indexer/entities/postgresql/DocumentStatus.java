package ai.semplify.indexer.entities.postgresql;

public enum DocumentStatus {
    PROCESSING("PROCESSING"),
    DONE("DONE");

    private String value;

    DocumentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
