package ai.semplify.commons.models.fileserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class File {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String url;

    private String name;

    private String module;

    private String contentType;

    @JsonIgnore
    private byte[] content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastModifiedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastModifiedBy;
}
