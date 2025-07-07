package com.codepractice.submission_service.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codepractice.submission_service.constants.Judge0Configurations;
import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.enums.SubmitResult;
import com.codepractice.submission_service.model.dto.external.ProblemResponse;
import com.codepractice.submission_service.model.dto.internal.request.Judge0Request;
import com.codepractice.submission_service.model.dto.internal.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionReponse;
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

    @Override
    public SubmissionReponse execute(SubmissionRequest solution, ExecuteType type) {
        SubmitStrategy strategy = submissionFactory.getSubmitStrategy(type);

        Map<String, String> judge0Params = initializeJudge0Params();

        List<Judge0Request> judge0Requests = initializeJudge0Body(solution);

        strategy.execute(judge0Requests, judge0Params);

        Submission savedSubmission = submissionRepository.save(
            Submission.builder()
                    .userId(solution.getUserId())
                    .problemId(solution.getProblemId())
                    .language(solution.getLanguage())
                    .result(SubmitResult.PROCESSING)
                    .time(0)
                    .memory(0)
                    .score(0)
                    .build()
        );

        return createDTO(savedSubmission);
    }

    private Map<String, String> initializeJudge0Params() {
        return Map.of(
            Judge0Configurations.TOKEN, Judge0Configurations.AUTH_TOKEN,
            Judge0Configurations.RETURN_FIELD, Judge0Configurations.FIELD_LIST
        );
    }

    private List<Judge0Request> initializeJudge0Body(SubmissionRequest solution) {
        ProblemResponse problem = problemService.getProblemById(solution.getProblemId());
        return problem.getSampleTests().stream().map(testcase ->
            Judge0Request.builder()
                .source_code(solution.getCode())
                .language_id(solution.getLanguage().getCode())
                .stdin(testcase.getInput())
                .expected_output(testcase.getOutput())
                .build()
        ).toList();
    }

    private SubmissionReponse createDTO(Submission source) {
        return SubmissionReponse.builder()
                .userId(source.getUserId())
                .problemId(source.getProblemId())
                .code(source.getCode())
                .result(source.getResult())
                .time(source.getTime())
                .memory(source.getMemory())
                .score(source.getScore())
                .build();
    }
}
