package ai.semplify.tasker.utils;

import ai.semplify.tasker.entities.postgresql.Task;
import ai.semplify.tasker.entities.postgresql.TaskParameter;
import ai.semplify.tasker.exceptions.MissingParameterException;
import ai.semplify.tasker.services.Params;
import lombok.var;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static String getOneParam(Task task, String paramName) throws MissingParameterException {
        var param = task.getParameters().stream().filter(p -> p.getName().equals(paramName)).findFirst()
                .orElseThrow(() -> new MissingParameterException("Missing parameter " + paramName));
        return param.getValue();
    }

    public static List<TaskParameter> getParams(Task task, String paramName) {
        return task.getParameters().stream().filter(p -> p.getName().equals(paramName))
                .collect(Collectors.toList());
    }
}
