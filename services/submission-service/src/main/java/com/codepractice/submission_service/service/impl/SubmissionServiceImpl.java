package com.codepractice.submission_service.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.submission_service.client.ProblemServiceClient;
import com.codepractice.submission_service.client.UserServiceClient;
import com.codepractice.submission_service.enums.SubmitResult;
import com.codepractice.submission_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.submission_service.model.dto.internal.UpdateProblemStatsRequest;
import com.codepractice.submission_service.model.dto.internal.UpdateUserStatsRequest;
import com.codepractice.submission_service.model.dto.request.ExecuteRequest;
import com.codepractice.submission_service.model.dto.request.RunRequest;
import com.codepractice.submission_service.model.dto.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.model.dto.response.ResultResponse;
import com.codepractice.submission_service.model.dto.response.RunResponse;
import com.codepractice.submission_service.model.dto.response.SubmissionResponse;
import com.codepractice.submission_service.model.entity.Submission;
import com.codepractice.submission_service.model.entity.Submission.Result;
import com.codepractice.submission_service.model.mapper.SubmissionMapper;
import com.codepractice.submission_service.repository.SubmissionRepository;
import com.codepractice.submission_service.service.Judge0Service;
import com.codepractice.submission_service.service.SubmissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
  private final Judge0Service judge0Service;
  private final ProblemServiceClient problemServiceClient;
  private final SubmissionRepository submissionRepository;
  private final SubmissionMapper submissionMapper;
  private final UserServiceClient userServiceClient;
  private final UserUtil userUtil;

  private static final int DECIMAL_SCALE = 2;

  /**
   * Get all submissions for a specific problem.
   * 
   * @param problemId the ID of the problem
   * @return a list of submission responses
   */
  @Override
  public List<SubmissionResponse> getSubmissions(String problemId) {
    Long userId = getCurrentUserId();
    log.info("Retrieving submissions for user ID: {} and problem ID: {}", userId, problemId);

    List<Submission> submissions = submissionRepository.findAllByUserIdAndProblemId(userId, problemId);

    if (CollectionUtils.isEmpty(submissions)) {
      log.debug("No submissions found for user ID: {} and problem ID: {}", userId, problemId);
      return List.of();
    }

    return submissions.stream()
        .map(submissionMapper::buildSubmissionDTO)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all results for a specific submission.
   * 
   * @param submissionId the ID of the submission
   * @return list of result responses
   * @throws AppException if submission not found
   */
  @Override
  public List<ResultResponse> getResultBySubmissionId(String submissionId) {
    log.info("Retrieving results for submission ID: {}", submissionId);

    Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> {
      log.warn("Submission not found with ID: {}", submissionId);
      return new AppException(ErrorCode.SUBMISSION_NOT_FOUND);
    });

    List<Result> resultDetails = submission.getResultDetails();
    if (CollectionUtils.isEmpty(resultDetails)) {
      log.debug("No result details found for submission ID: {}", submissionId);
      return List.of();
    }

    return resultDetails.stream()
        .map(submissionMapper::buildResultDTO)
        .collect(Collectors.toList());
  }

  /**
   * Run a solution.
   * 
   * @param solution the solution request
   * @return the run response
   */
  @Override
  public RunResponse runSolution(RunRequest solution) {
    log.info("Running solution for request: {}", solution);

    try {
      ExecuteRequest executeRequests = submissionMapper.buildRunRequest(solution);

      Judge0Response judge0Response = judge0Service.execute(executeRequests);

      return submissionMapper.buildRunDTO(judge0Response);
    } catch (Exception e) {
      log.error("Error occurred while running solution: {}", e.getMessage());
      throw new AppException(ErrorCode.EXECUTION_FAILED, e.getMessage());
    }
  }

  /**
   * Submit a solution.
   * 
   * @param solutions the submission request
   * @return the submission response
   */
  @Override
  @Transactional
  public SubmissionResponse submitSolution(SubmissionRequest solutions) {
    log.info("Submitting solution for request: {}", solutions);

    try {
      ProblemClientTestCaseResponse problem = problemServiceClient.getProblemById(solutions.getProblemId());

      // State 1: build judge0 requests
      List<ExecuteRequest> executeRequests = submissionMapper.buildSubmissionRequests(problem, solutions);

      // State 2: execute request and save new submission result
      List<Result> testResults = executeTestCases(executeRequests);

      // State 3: create new submission
      Submission submission = createNewSubmission(solutions, testResults);

      // State 4: update statistics
      updateUserStatsAsync(submission);
      updateProblemStatsAsync(submission);

      return submissionMapper.buildSubmissionDTO(submission);
    } catch (Exception e) {
      log.error("Error occurred while submitting solution: {}", e.getMessage());
      throw new AppException(ErrorCode.EXECUTION_FAILED, e.getMessage());
    }
  }

  // ======================== UTILS OPERATIONS ========================

  /**
   * Executes all test cases and returns results.
   * 
   * @param executeRequests list of execution requests
   * @return list of test results
   */
  private List<Result> executeTestCases(List<ExecuteRequest> executeRequests) {
    log.debug("Executing {} test cases", executeRequests.size());

    return executeRequests.stream()
        .map(this::executeAndBuildResult)
        .collect(Collectors.toList());
  }

  /**
   * Executes a single test case and builds the result.
   */
  private Result executeAndBuildResult(ExecuteRequest request) {
    try {
      Judge0Response response = judge0Service.execute(request);
      SubmitResult result = SubmitResult.getByCode(response.getStatus().getId());

      return Result.builder()
          .result(result)
          .token(response.getToken())
          .stderr(response.getStderr())
          .stdout(response.getStdout())
          .compilerOutput(response.getCompile_output())
          .message(response.getMessage())
          .testCaseOrder(request.getTestCaseOrder())
          .time(response.getTime())
          .memory(response.getMemory())
          .point(SubmitResult.ACCEPTED.equals(result) ? request.getTestCasePoint() : 0.0)
          .build();

    } catch (Exception e) {
      log.error("Failed to execute test case order {}: {}",
          request.getTestCaseOrder(), e.getMessage());

      // Return a failed result instead of throwing exception
      return Result.builder()
          .result(SubmitResult.RUNTIME_ERROR)
          .testCaseOrder(request.getTestCaseOrder())
          .message("Execution failed: " + e.getMessage())
          .time(0.0)
          .memory(0.0)
          .point(0.0)
          .build();
    }
  }

  /**
   * Gets current user ID with proper error handling.
   */
  private Long getCurrentUserId() {
    try {
      String userIdStr = userUtil.getCurrentUserId();
      return Long.parseLong(userIdStr);
    } catch (NumberFormatException e) {
      String userIdStr = userUtil.getCurrentUserId();
      log.error("Invalid user ID format: {}", userIdStr);
      throw new AppException(ErrorCode.USER_NOT_FOUND);
    }
  }

  /**
   * Create a new submission with processing status.
   * 
   * @param solution
   * @param results
   * @return
   */
  @Transactional
  private Submission createNewSubmission(SubmissionRequest request, List<Result> results) {
    Long userId = Long.parseLong(userUtil.getCurrentUserId());
    SubmissionMetrics metrics = calculateSubmissionMetrics(results);

    log.info("Creating new submission for user ID: {}", userId);

    Submission submission = Submission.builder()
        .userId(userId)
        .problemId(request.getProblemId())
        .language(request.getLanguage())
        .code(request.getCode())
        .result(metrics.overallResult)
        .resultDetails(results)
        .totalTime(metrics.totalTime)
        .totalMemory(metrics.totalMemory)
        .score(metrics.totalScore)
        .build();

    return submissionRepository.save(submission);
  }

  /**
   * Calculates submission metrics from test results.
   */
  private SubmissionMetrics calculateSubmissionMetrics(List<Result> results) {
    double totalTime = 0.0;
    double totalMemory = 0.0;
    double totalScore = 0.0;
    SubmitResult overallResult = SubmitResult.ACCEPTED;

    for (Result result : results) {
      totalTime += result.getTime();
      totalMemory += result.getMemory();
      totalScore += result.getPoint();

      // If any test case fails, mark overall submission as failed
      if (!SubmitResult.ACCEPTED.equals(result.getResult())) {
        overallResult = result.getResult(); // Use the specific failure reason
      }
    }

    return new SubmissionMetrics(
        roundToDecimalPlaces(totalTime),
        roundToDecimalPlaces(totalMemory),
        roundToDecimalPlaces(totalScore),
        overallResult);
  }

  /**
   * Rounds double values to specified decimal places for consistency.
   */
  private double roundToDecimalPlaces(double value) {
    return BigDecimal.valueOf(value)
        .setScale(DECIMAL_SCALE, RoundingMode.HALF_UP)
        .doubleValue();
  }

  /**
   * Update user statistics after submission.
   * 
   * @param submission
   */
  private void updateUserStatsAsync(Submission submission) {
    UpdateUserStatsRequest userUpdateStatsRequest = UpdateUserStatsRequest.builder()
        .id(submission.getUserId())
        .newAchievePoint(submission.getScore())
        .build();

    userServiceClient.updateUserStats(userUpdateStatsRequest);
  }

  /**
   * Updates the problem statistics after submission.
   * 
   * @param submission
   */
  private void updateProblemStatsAsync(Submission submission) {
    UpdateProblemStatsRequest problemUpdateStatsRequest = UpdateProblemStatsRequest.builder()
        .id(submission.getProblemId())
        .isAccepted(SubmitResult.ACCEPTED.equals(submission.getResult()))
        .build();

    problemServiceClient.updateProblemStats(problemUpdateStatsRequest);
  }

  private static class SubmissionMetrics {
    final double totalTime;
    final double totalMemory;
    final double totalScore;
    final SubmitResult overallResult;

    SubmissionMetrics(double totalTime, double totalMemory, double totalScore, SubmitResult overallResult) {
      this.totalTime = totalTime;
      this.totalMemory = totalMemory;
      this.totalScore = totalScore;
      this.overallResult = overallResult;
    }
  }
}
