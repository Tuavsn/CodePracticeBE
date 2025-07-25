package com.codepractice.problem_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
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
                    .example(request.getExample())
                    .tags(request.getTags())
                    .hints(request.getHints())
                    .codeTemplates(request.getCodeTemplates())
                    .sampleTests(request.getSampleTests())
                    .reactionCount(0)
                    .commentCount(0)
                    .totalSubmissions(0)
                    .totalAcceptedSubmissions(0)
                    .totalScore(request.getTotalScore())
                    .timeLimitSeconds(request.getTimeLimitSeconds())
                    .memoryLimitMb(request.getMemoryLimitMb())
                    .build()
            );
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
            log.info("Deleted problem: {}", id);
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
    public List<ProblemResponse> getAll() {
        log.info("Get all problems");
        try {
            List<Problem> problems = problemRepository.findAllByIsDeleted(false);
            log.info("Found {} problems", problems.size());
            return problems.stream().map(this::createDTO).toList();
        } catch (Exception e) {
            log.error("Failed to get all problems: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public ProblemResponse getById(String id) {
        log.info("Get problem: {}", id);
        try {
            Problem existedProblem = problemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));
            
            return createDTO(existedProblem);
        } catch (Exception e) {
            log.error("Failed to get problem {}: {}", id, e.getMessage());
            throw e;
        }
    }

    private ProblemResponse createDTO(Problem source) {
        return ProblemResponse
                    .builder()
                    .id(source.getId())
                    .title(source.getTitle())
                    .description(source.getDescription())
                    .thumbnail(source.getThumbnail())
                    .constraints(source.getConstraints())
                    .difficulty(source.getDifficulty())
                    .example(source.getExample())
                    .tags(source.getTags())
                    .hints(source.getHints())
                    .codeTemplates(source.getCodeTemplates())
                    .sampleTests(source.getSampleTests())
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
        if (source.getExample() != null && !source.getExample().isEmpty()) {
            target.setExample(source.getExample());
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