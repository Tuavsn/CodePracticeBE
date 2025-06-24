package com.codepractice.common_lib.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class JpaBaseEntity extends BaseEntity<JpaBaseEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    protected String id;
    
    @Column(name = "created_by", updatable = false)
    protected String createdBy;
    
    @Column(name = "updated_by")
    protected String updatedBy;
    
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
    
    @Column(name = "is_deleted", nullable = false)
    protected boolean isDeleted = false;
    
    @Version
    @Column(name = "version")
    protected Long version;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
