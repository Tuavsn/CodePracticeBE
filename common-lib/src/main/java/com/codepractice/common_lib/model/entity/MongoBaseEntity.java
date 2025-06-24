package com.codepractice.common_lib.model.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
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
public class MongoBaseEntity extends BaseEntity<MongoBaseEntity> {
    @Id
    protected String id;
    
    @CreatedBy
    @Field("created_by")
    protected String createdBy;
    
    @LastModifiedBy
    @Field("updated_by")
    protected String updatedBy;
    
    @CreatedDate
    @Field("created_at")
    protected LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    protected LocalDateTime updatedAt;
    
    @Field("is_deleted")
    @Indexed
    protected boolean isDeleted = false;
    
    @Version
    @Field("version")
    protected Long version;
}
