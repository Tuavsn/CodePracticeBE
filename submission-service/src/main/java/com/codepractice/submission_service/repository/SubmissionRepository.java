package com.codepractice.submission_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.submission_service.model.entity.Submission;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    
}
