package com.codepractice.problem_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.problem_service.model.entity.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    public List<Comment> findAllByProblemIdAndIsDeleted(String id, boolean isDeleted);

    public List<Comment> findAllByAuthor_IdAndIsDeleted(String id, boolean isDeleted);
}
