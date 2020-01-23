package ai.semplify.fileserver.entities;

import java.util.Date;

public interface FileInfo {
    Long getId();

    String getName();

    String getModule();

    String getContentType();

    Date getCreatedDate();

    Date getLastModifiedDate();
}
