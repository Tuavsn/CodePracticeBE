package com.codepractice.problem_service.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.problem_service.enums.ProblemDifficulty;
import com.codepractice.problem_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.problem_service.model.dto.internal.UpdateProblemStatsRequest;
import com.codepractice.problem_service.model.dto.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.response.ProblemResponse;
import com.codepractice.problem_service.model.entity.Problem;
import com.codepractice.problem_service.repository.ProblemRepository;
import com.codepractice.problem_service.service.ProblemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
  private final ProblemRepository problemRepository;
  private final MongoTemplate mongoTemplate;

  // ======================== PROBLEM CRUD OPERATIONS ========================
  @Override
  public Page<ProblemResponse> getAll(String title, List<String> tags, ProblemDifficulty difficulty,
      Pageable pageable) {
    log.debug("Retrieving all active problem with filter - topic: {}, difficulty: {}, page: {}, size: {}", tags,
        difficulty, pageable.getPageNumber(), pageable.getPageSize());

    Query query = buildProblemFilterQuery(title, tags, difficulty, null, pageable);

    Query countQuery = buildProblemFilterQuery(title, tags, difficulty, null, null);

    List<Problem> problemsList = mongoTemplate.find(query, Problem.class);

    long total = mongoTemplate.count(countQuery, Problem.class);

    Page<Problem> problems = new PageImpl<>(problemsList, pageable, total);

    log.info("Found {} problems", total);

    return problems.map(this::createDTO);
  }

  @Override
  public ProblemResponse getById(String id) {
    log.info("Retrieving problem with ID: {}", id);

    Problem existedProblem = problemRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

    return createDTO(existedProblem);
  }

  @Override
  public ProblemClientTestCaseResponse getTestCaseByProblemId(String id) {
    log.info("Retrieving problem test cases with ID: {}", id);

    Problem existedProblem = problemRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

    return ProblemClientTestCaseResponse.builder()
        .id(existedProblem.getId())
        .sampleTests(existedProblem.getSampleTests())
        .timeLimitSeconds(existedProblem.getTimeLimitSeconds())
        .memoryLimitMb(existedProblem.getMemoryLimitMb())
        .totalScore(existedProblem.getTotalScore())
        .build();
  }

  @Override
  @Transactional
  public ProblemResponse save(ProblemRequest request) {
    log.info("Create new problem");

    try {
      Problem savedProblem = problemRepository.save(
          Problem.builder()
              .title(request.getTitle())
              .description(request.getDescription())
              .thumbnail(request.getThumbnail())
              .constraints(request.getConstraints())
              .difficulty(request.getDifficulty())
              .examples(request.getExamples())
              .tags(request.getTags())
              .hints(request.getHints())
              .codeTemplates(request.getCodeTemplates())
              .sampleTests(request.getSampleTests())
              .reactions(new HashSet<>())
              .reactionCount(0)
              .commentCount(0)
              .totalSubmissions(0)
              .totalAcceptedSubmissions(0)
              .totalScore(request.getTotalScore())
              .timeLimitSeconds(request.getTimeLimitSeconds())
              .memoryLimitMb(request.getMemoryLimitMb())
              .build());

      log.info("Created problem with ID: {}", savedProblem.getId());

      return createDTO(savedProblem);
    } catch (Exception e) {
      log.error("Failed to create problem: {}", e.getMessage());

      throw e;
    }
  }

  @Override
  @Transactional
  public ProblemResponse update(String id, ProblemRequest request) {
    log.info("Update problem: {}", id);
    try {
      Problem existedProblem = problemRepository.findById(id)
          .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

      updateProblem(request, existedProblem);

      log.info("Updated problem: {}", id);

      return createDTO(existedProblem);
    } catch (Exception e) {
      log.error("Failed to update problem {}: {}", id, e.getMessage());
      throw e;
    }
  }

  @Override
  @Transactional
  public void delete(String id) {
    log.info("Delete problem: {}", id);

    try {
      Problem existedProblem = problemRepository.findById(id)
          .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

      existedProblem.setDeleted(true);

      problemRepository.save(existedProblem);
    } catch (Exception e) {
      log.error("Failed to delete problem {}: {}", id, e.getMessage());
      throw e;
    }
  }

  @Override
  @Transactional
  public void hardDelete(String id) {
    log.info("Hard delete problem: {}", id);
    try {
      Problem existedProblem = problemRepository.findById(id)
          .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

      problemRepository.delete(existedProblem);
      log.info("Hard deleted problem: {}", id);
    } catch (Exception e) {
      log.error("Failed to hard delete problem {}: {}", id, e.getMessage());
      throw e;
    }
  }

  @Override
  @Transactional
  public void updateProblemStats(UpdateProblemStatsRequest request) {
    String id = request.getId();

    log.info("Updating user stats for problem ID: {}", id);

    Optional<Problem> existProblem = problemRepository.findById(id);

    existProblem.ifPresentOrElse(problem -> {
      updateStats(problem, request.getIsAccepted());
      log.info("Problem stats updated successfully for ID: {}", id);
    }, () -> {
      log.error("Problem not found for ID: {}", id);
      throw new AppException(ErrorCode.PROBLEM_NOT_FOUND);
    });
  }

  // ======================== UTILS OPERATIONS ========================

  /**
   * Update problem statistics
   * 
   * @param problem
   * @param isAccepted
   */
  private void updateStats(Problem problem, boolean isAccepted) {
    problem.setTotalSubmissions(problem.getTotalSubmissions() + 1);
    if (isAccepted) {
      problem.setTotalAcceptedSubmissions(problem.getTotalAcceptedSubmissions() + 1);
    }
    problemRepository.save(problem);
  }

  /**
   * Build MongoTamplate Query
   * 
   * @param title
   * @param topics
   * @param difficulty
   * @param isDeleted
   * @param pageable
   * @return
   */
  private Query buildProblemFilterQuery(String title, List<String> tags, ProblemDifficulty difficulty,
      Boolean isDeleted, Pageable pageable) {
    List<Criteria> criteriaList = new ArrayList<>();

    if (title != null && !title.isBlank()) {
      criteriaList.add(Criteria.where("title").regex(title, "i"));
    }
    if (tags != null && !tags.isEmpty()) {
      criteriaList.add(Criteria.where("tags").in(tags));
    }
    if (difficulty != null) {
      criteriaList.add(Criteria.where("difficulty").is(difficulty));
    }
    if (isDeleted != null) {
      criteriaList.add(Criteria.where("isDeleted").is(isDeleted));
    }

    Criteria finalCriteria = new Criteria();

    if (!criteriaList.isEmpty()) {
      finalCriteria.andOperator(criteriaList.toArray(new Criteria[0]));
    }

    Query query = new Query(finalCriteria);

    if (pageable != null) {
      query.with(pageable);
    }

    return query;
  }

  /**
   * Map to response DTO
   * 
   * @param source
   * @return
   */
  private ProblemResponse createDTO(Problem source) {
    return ProblemResponse
        .builder()
        .id(source.getId())
        .title(source.getTitle())
        .description(source.getDescription())
        .thumbnail(source.getThumbnail())
        .constraints(source.getConstraints())
        .difficulty(source.getDifficulty())
        .examples(source.getExamples())
        .tags(source.getTags())
        .hints(source.getHints())
        .codeTemplates(source.getCodeTemplates())
        .reactions(source.getReactions())
        .reactionCount(source.getReactionCount())
        .commentCount(source.getCommentCount())
        .totalSubmissions(source.getTotalSubmissions())
        .totalAcceptedSubmissions(source.getTotalAcceptedSubmissions())
        .timeLimitSeconds(source.getTimeLimitSeconds())
        .memoryLimitMb(source.getMemoryLimitMb())
        .totalScore(source.getTotalScore())
        .createdAt(source.getCreatedAt())
        .updatedAt(source.getUpdatedAt())
        .build();
  }

  /**
   * Update fields
   * 
   * @param source
   * @param target
   */
  private void updateProblem(ProblemRequest source, Problem target) {
    if (source.getTitle() != null) {
      target.setTitle(source.getTitle());
    }
    if (source.getDescription() != null) {
      target.setDescription(source.getDescription());
    }
    if (source.getThumbnail() != null) {
      target.setThumbnail(source.getThumbnail());
    }
    if (source.getConstraints() != null && !source.getConstraints().isEmpty()) {
      target.setConstraints(source.getConstraints());
    }
    if (source.getDifficulty() != null) {
      target.setDifficulty(source.getDifficulty());
    }
    if (source.getCodeTemplates() != null && !source.getCodeTemplates().isEmpty()) {
      target.setCodeTemplates(source.getCodeTemplates());
    }
    if (source.getExamples() != null && !source.getExamples().isEmpty()) {
      target.setExamples(source.getExamples());
    }
    if (source.getTags() != null && !source.getTags().isEmpty()) {
      target.setTags(source.getTags());
    }
    if (source.getHints() != null && !source.getHints().isEmpty()) {
      target.setHints(source.getHints());
    }
    if (source.getSampleTests() != null && !source.getSampleTests().isEmpty()) {
      target.setSampleTests(source.getSampleTests());
    }
    if (source.getTimeLimitSeconds() >= 0) {
      target.setTimeLimitSeconds(source.getTimeLimitSeconds());
    }
    if (source.getMemoryLimitMb() >= 0) {
      target.setMemoryLimitMb(source.getMemoryLimitMb());
    }
    if (source.getTotalScore() >= 0) {
      target.setTotalScore(source.getTotalScore());
    }
  }
}