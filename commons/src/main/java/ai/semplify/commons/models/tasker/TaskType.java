package ai.semplify.commons.models.tasker;

public enum TaskType {

    FilesIntegration("FilesIntegration"),
    FileAnnotation("FileAnnotation"),
    UrlCrawler("UrlCrawler");

    private String value;

    TaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
