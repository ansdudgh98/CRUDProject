package hoy.project.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Column(columnDefinition = "tinyint(1) default 1")
    private boolean active;
}
