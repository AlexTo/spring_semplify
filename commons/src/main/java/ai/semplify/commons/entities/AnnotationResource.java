package ai.semplify.commons.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "semplify_annotation_resources")
@EntityListeners(AuditingEntityListener.class)
public class AnnotationResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uri;

    private String support;

    private String types;

    private String surfaceForm;

    private String prefLabel;

    private Integer _offset;

    private Double similarityScore;

    private String percentageOfSecondRank;

    private String source;

    private String context;

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
}
