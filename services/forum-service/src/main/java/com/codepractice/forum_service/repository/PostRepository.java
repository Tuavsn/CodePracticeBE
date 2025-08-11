package com.codepractice.forum_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.forum_service.model.entity.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
}