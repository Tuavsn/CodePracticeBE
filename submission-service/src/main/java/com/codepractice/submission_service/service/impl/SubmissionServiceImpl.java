package com.codepractice.submission_service.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.submission_service.constants.Judge0Configurations;
import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.enums.SubmitResult;
import com.codepractice.submission_service.model.dto.external.ProblemResponse;
import com.codepractice.submission_service.model.dto.external.ProblemResponse.TestCase;
import com.codepractice.submission_service.model.dto.internal.request.Judge0Request;
import com.codepractice.submission_service.model.dto.internal.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.internal.response.Judge0Response;
import com.codepractice.submission_service.model.dto.internal.response.ResultResponse;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionResponse;
import com.codepractice.submission_service.model.entity.Result;
import com.codepractice.submission_service.model.entity.Submission;
import com.codepractice.submission_service.repository.ResultRepository;
import com.codepractice.submission_service.repository.SubmissionRepository;
import com.codepractice.submission_service.service.SubmissionService;
import com.codepractice.submission_service.service.SubmitStrategy;
import com.codepractice.submission_service.service.client.ProblemServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionFactory submissionFactory;
    private final ProblemServiceClient problemService;
    private final SubmissionRepository submissionRepository;
    private final ResultRepository resultRepository;
    private final UserUtil userUtil;

    @Override
    public List<SubmissionResponse> getSubmissions(String problemId) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Retrieve submissions for user ID: {}", userId);
        
        return submissionRepository.findAllByUserIdAndProblemId(userId, problemId).stream()
                .map(submission -> createSubmissionDTO(submission)).toList();
    }

    @Override
    public List<ResultResponse> getResultBySubmissionId(String submissionId) {
        return resultRepository.findAllBySubmissionId(submissionId).stream().map(result -> createResultDTO(result))
                .toList();
    }

    @Override
    @Transactional
    public SubmissionResponse execute(SubmissionRequest solution, ExecuteType type) {
        ProblemResponse problem = problemService.getProblemById(solution.getProblemId());

        SubmitStrategy strategy = submissionFactory.getSubmitStrategy(type);

        Map<String, String> judge0Params = initializeJudge0Params();

        List<Judge0Request> judge0Requests = initializeJudge0Body(problem, solution);

        Submission savedSubmission = createSubmission(solution);

        List<Judge0Response> judge0Responses = strategy.execute(judge0Requests, judge0Params);

        List<Result> savedResults = createResult(savedSubmission, judge0Responses);

        caculateSubmissionResult(problem, savedSubmission, savedResults);

        return createSubmissionDTO(savedSubmission);
    }

    private Map<String, String> initializeJudge0Params() {
        return Map.of(
                Judge0Configurations.TOKEN, Judge0Configurations.AUTH_TOKEN,
                Judge0Configurations.RETURN_FIELD, Judge0Configurations.FIELD_LIST);
    }

    private List<Judge0Request> initializeJudge0Body(ProblemResponse problem, SubmissionRequest solution) {
        return problem.getSampleTests().stream().map(testcase -> Judge0Request.builder()
                .source_code(solution.getCode())
                .language_id(solution.getLanguage().getCode())
                .stdin(testcase.getInput())
                .expected_output(testcase.getOutput())
                .build()).toList();
    }

    @Transactional
    private void caculateSubmissionResult(ProblemResponse problem, Submission submission, List<Result> results) {
        if (problem.getSampleTests().size() != results.size()) {
            throw new AppException(ErrorCode.MISMATCH_BETWEEN_SAMPLETEST_AND_RESULT);
        }

        double totalExecuteTime = 0;
        double totalExecuteMemory = 0;
        double totalScore = 0;
        SubmitResult submissionResult = SubmitResult.ACCEPTED;

        for (int i = 0; i < results.size(); ++i) {
            totalExecuteTime += results.get(i).getTime();
            totalExecuteMemory += results.get(i).getMemory();
            totalScore += getScore(problem.getSampleTests().get(i), results.get(i));

            if (!submissionResult.ACCEPTED.equals(results.get(i).getResult())) {
                submissionResult = submissionResult.WRONG_ANSWER;
            }
        }

        submission.setTime(totalExecuteTime);
        submission.setMemory(totalExecuteMemory);
        submission.setScore(totalScore);
        submission.setResult(submissionResult);

        submissionRepository.save(submission);
    }

    private double getScore(TestCase testCase, Result result) {
        return result.getResult().equals(SubmitResult.ACCEPTED) ? testCase.getPoint() : 0;
    }

    @Transactional
    private Submission createSubmission(SubmissionRequest solution) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Creating new submission for user ID: {}", userId);

        return submissionRepository.save(
                Submission.builder()
                        .userId(userId)
                        .problemId(solution.getProblemId())
                        .language(solution.getLanguage())
                        .code(solution.getCode())
                        .result(SubmitResult.PROCESSING)
                        .time(0)
                        .memory(0)
                        .score(0)
                        .build());
    }

    @Transactional
    private List<Result> createResult(Submission submission, List<Judge0Response> judge0Responses) {
        return judge0Responses.stream().<Result>map(response -> resultRepository.save(
                Result.builder()
                        .submissionId(submission.getId())
                        .result(SubmitResult.getByCode(response.getStatus_id()))
                        .token(response.getToken())
                        .error(response.getStderr())
                        .stdout(response.getStdout())
                        .time(response.getTime())
                        .memory(response.getMemory())
                        .point(0)
                        .build()))
                .toList();
    }

    private SubmissionResponse createSubmissionDTO(Submission source) {
        return SubmissionResponse.builder()
                .id(source.getId())
                .userId(source.getUserId())
                .problemId(source.getProblemId())
                .language(source.getLanguage())
                .code(source.getCode())
                .result(source.getResult())
                .time(source.getTime())
                .memory(source.getMemory())
                .score(source.getScore())
                .build();
    }

    private ResultResponse createResultDTO(Result source) {
        return ResultResponse.builder()
                .submissionId(source.getSubmissionId())
                .result(source.getResult())
                .error(source.getError())
                .stdout(source.getStdout())
                .time(source.getTime())
                .memory(source.getMemory())
                .build();
    }
}
