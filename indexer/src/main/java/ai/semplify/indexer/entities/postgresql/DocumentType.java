package ai.semplify.indexer.entities.postgresql;

public enum DocumentType {

    FILE("FILE"),
    URL("URL");

    private String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
