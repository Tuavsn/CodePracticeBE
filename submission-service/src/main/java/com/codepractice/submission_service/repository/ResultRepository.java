package com.codepractice.submission_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.submission_service.model.entity.Result;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
    public List<Result> findAllBySubmissionId(String submissionId);
}
