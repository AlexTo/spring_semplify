package ai.semplify.commons.models.tasker;

public enum TaskStatus {
    SUBTASKS_SPAWNED("SUBTASKS_SPAWNED"),
    FINISHED("FINISHED");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

