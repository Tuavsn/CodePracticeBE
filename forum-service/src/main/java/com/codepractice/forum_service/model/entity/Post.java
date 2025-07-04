package com.codepractice.forum_service.model.entity;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Post extends BaseEntity {
    private Author author;
    private String title;
    private String thumbnail;
    private String content;
    private Set<PostImage> images;
    private List<String> topics;
    private Set<Reaction> reactions;
}
