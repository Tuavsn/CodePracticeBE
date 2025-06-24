package com.codepractice.common_lib.utils;

import com.codepractice.common_lib.constants.DatabaseType;
import com.codepractice.common_lib.model.entity.JpaBaseEntity;
import com.codepractice.common_lib.model.entity.MongoBaseEntity;

/**
 * Use Java Reflection Api to get BaseEntity
 */
public class EntityFactory {
    /**
     * Get BaseEntity Class
     * @param type
     * @return BaseEntity Class
     */
    public static Class<?> getBaseEntityClass(DatabaseType type) {
        return switch (type) {
            case JPA -> JpaBaseEntity.class;
            case MONGO -> MongoBaseEntity.class;
        };
    }

    /**
     * Get BaseEntity Class name
     * @param type
     * @return BaseEntity Class name
     */
    public static String getBaseEntityClassName(DatabaseType type) {
        return switch (type) {
            case JPA -> "com.codepractice.common_lib.model.entity.JpaBaseEntity";
            case MONGO -> "com.codepractice.common_lib.model.entity.MongoBaseEntity";
        };
    }
}
