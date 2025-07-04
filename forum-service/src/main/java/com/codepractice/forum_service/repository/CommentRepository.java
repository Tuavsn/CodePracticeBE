package com.codepractice.forum_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.forum_service.model.entity.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    public List<Comment> findAllByPostIdAndIsDeleted(String id, boolean isDeleted);
    
    public List<Comment> findAllByAuthor_UserIdAndIsDeleted(String id, boolean isDeleted);
}