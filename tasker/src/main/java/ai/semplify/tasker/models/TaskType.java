package ai.semplify.tasker.models;

public enum TaskType {

    FilesIntegration("FilesIntegration"),
    FileAnnotation("FileAnnotation");

    private String value;

    TaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
