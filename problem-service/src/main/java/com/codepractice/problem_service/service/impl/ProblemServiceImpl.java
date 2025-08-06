package com.codepractice.problem_service.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import com.codepractice.problem_service.model.dto.internal.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;
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
    public Page<ProblemResponse> getAll(String title, List<String> topic, ProblemDifficulty difficulty,
            Pageable pageable) {
        log.debug("Retrieving all active problem with filter - topic: {}, difficulty: {}, page: {}, size: {}", topic,
                difficulty, pageable.getPageNumber(), pageable.getPageSize());

        Query query = buildProblemFilterQuery(title, topic, difficulty, null, pageable);

        List<Problem> problemsList = mongoTemplate.find(query, Problem.class);

        long total = problemsList.size();

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

    // ======================== UTILS OPERATIONS ========================

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
    private Query buildProblemFilterQuery(String title, List<String> topics, ProblemDifficulty difficulty,
            Boolean isDeleted, Pageable pageable) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            criteriaList.add(Criteria.where("title").regex(title, "i"));
        }
        if (topics != null && !topics.isEmpty()) {
            criteriaList.add(Criteria.where("topics").in(topics));
        }
        if (difficulty != null) {
            criteriaList.add(Criteria.where("author.id").is(difficulty));
        }
        if (isDeleted != null) {
            criteriaList.add(Criteria.where("isDeleted").is(isDeleted));
        }

        Criteria finalCriteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            finalCriteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        return new Query(finalCriteria).with(pageable);
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
                .sampleTests(source.getSampleTests())
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