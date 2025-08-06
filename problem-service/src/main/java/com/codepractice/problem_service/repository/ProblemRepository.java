package com.codepractice.problem_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.problem_service.model.entity.Problem;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
}
