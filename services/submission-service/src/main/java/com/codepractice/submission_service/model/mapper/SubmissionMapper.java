package com.codepractice.submission_service.model.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.codepractice.submission_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.submission_service.model.dto.request.ExecuteRequest;
import com.codepractice.submission_service.model.dto.request.RunRequest;
import com.codepractice.submission_service.model.dto.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.model.dto.response.ResultResponse;
import com.codepractice.submission_service.model.dto.response.RunResponse;
import com.codepractice.submission_service.model.dto.response.SubmissionResponse;
import com.codepractice.submission_service.model.entity.Submission;
import com.codepractice.submission_service.model.entity.Submission.Result;

@Component
public class SubmissionMapper {

  /**
   * Build a SubmissionResponse DTO from a Submission entity.
   * 
   * @param source
   * @return
   */
  public SubmissionResponse buildSubmissionDTO(Submission source) {
    return SubmissionResponse.builder()
        .id(source.getId())
        .userId(source.getUserId())
        .problemId(source.getProblemId())
        .result(source.getResult())
        .language(source.getLanguage())
        .code(source.getCode())
        .time(source.getTotalTime())
        .memory(source.getTotalMemory())
        .score(source.getScore())
        .createdAt(source.getCreatedAt())
        .build();
  }

  /**
   * Build a ResultResponse DTO from a Result entity.
   * 
   * @param source
   * @return
   */
  public ResultResponse buildResultDTO(Result source) {
    return ResultResponse.builder()
        .result(source.getResult())
        .error(source.getStderr())
        .stdout(source.getStdout())
        .compilerOutput(source.getCompilerOutput())
        .message(source.getMessage())
        .time(source.getTime())
        .memory(source.getMemory())
        .createdAt(source.getCreatedAt())
        .build();
  }

  /**
   * Build a RunResponse DTO from a Judge0Response entity.
   * 
   * @param response
   * @return
   */
  public RunResponse buildRunDTO(Judge0Response response) {
    return RunResponse.builder()
        .stdout(response.getStdout())
        .stderr(response.getStderr())
        .compile_output(response.getCompile_output())
        .message(response.getMessage())
        .success(response.getStatus().getId() == 3)
        .build();
  }

  public ExecuteRequest buildRunRequest(RunRequest solution) {
    return ExecuteRequest.builder()
        .code(solution.getCode())
        .input(solution.getInput())
        .expectedOutput(null)
        .language(solution.getLanguage())
        .build();
  }

  /**
   * Build a list of ExecuteRequest DTOs from a ProblemClientTestCaseResponse and
   * SubmissionRequest.
   * 
   * @param problem
   * @param solutions
   * @return
   */
  public List<ExecuteRequest> buildSubmissionRequests(ProblemClientTestCaseResponse problem,
      SubmissionRequest solutions) {
    return problem.getSampleTests().stream()
        .map(testCase -> ExecuteRequest.builder()
            .code(solutions.getCode())
            .input(testCase.getInput())
            .expectedOutput(testCase.getOutput())
            .language(solutions.getLanguage())
            .testCaseOrder(testCase.getOrder())
            .testCasePoint(testCase.getPoint())
            .build())
        .toList();
  }

}
