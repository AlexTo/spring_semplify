package ai.semplify.entityhub.models;

import lombok.Data;

@Data
public class File {
    private Long id;
    private String url;
    private String name;
    private String module;
    private String contentType;
}
