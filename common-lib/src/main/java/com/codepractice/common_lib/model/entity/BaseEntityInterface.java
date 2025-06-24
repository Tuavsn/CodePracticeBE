package com.codepractice.common_lib.model.entity;

import java.time.LocalDateTime;

public interface BaseEntityInterface {
    String getId();
    void setId(String id);
    
    String getCreatedBy();
    void setCreatedBy(String createdBy);
    
    String getUpdatedBy();
    void setUpdatedBy(String updatedBy);
    
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    
    LocalDateTime getUpdatedAt();
    void setUpdatedAt(LocalDateTime updatedAt);
    
    boolean isDeleted();
    void setDeleted(boolean deleted);
    
    Long getVersion();
    void setVersion(Long version);
    
    void softDelete();
    void restore();
}
