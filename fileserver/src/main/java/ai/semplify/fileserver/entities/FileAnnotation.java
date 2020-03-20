package ai.semplify.fileserver.entities;

import ai.semplify.commons.entities.AnnotationResource;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "semplify_file_server_files_annotation")
@EntityListeners(AuditingEntityListener.class)
public class FileAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private File file;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AnnotationResource> annotationResources;

    @NotNull
    private String status;

    @NotNull
    @CreatedDate
    private Date createdDate;

    @NotNull
    @LastModifiedDate
    private Date lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private Long version;
}
