package com.codepractice.submission_service.service.impl;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.codepractice.common_lib.utils.StringUtil;
import com.codepractice.submission_service.constants.Judge0Configurations;
import com.codepractice.submission_service.enums.SubmitLanguage;
import com.codepractice.submission_service.model.dto.request.Judge0Body;
import com.codepractice.submission_service.model.dto.request.Judge0Request;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.service.RestTemplateService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public abstract class Judge0Execute {
  private final RestTemplateService restTemplateService;

  /**
   * Executes the request.
   * 
   * @param <R>
   * @param requests
   * @param typeRef
   * @return
   */
  protected Judge0Response sendToJudge0(Judge0Request request) {
    String sumitEndpoint = Judge0Configurations.getSubmissionEndpoint();

    ParameterizedTypeReference<Judge0Response> typeRef = new ParameterizedTypeReference<Judge0Response>() {
    };

    Judge0Response response = restTemplateService.post(
        sumitEndpoint,
        request.getJudge0Body(),
        request.getJudge0Params(),
        typeRef);

    return buildJudge0Response(response);
  }

  /**
   * Builds the Judge0 request.
   * 
   * @param params
   * @param body
   * @return
   */
  protected Judge0Request buildJudge0Request(Map<String, String> params, Judge0Body body) {
    return Judge0Request.builder().judge0Params(params).judge0Body(body).build();
  }

  protected Judge0Response buildJudge0Response(Judge0Response response) {
    return Judge0Response.builder()
        .language_id(response.getLanguage_id())
        .stdout(StringUtil.decodeBase64(response.getStdout()))
        .stderr(StringUtil.decodeBase64(response.getStderr()))
        .token(response.getToken())
        .time(response.getTime())
        .memory(response.getMemory())
        .compile_output(StringUtil.decodeBase64(response.getCompile_output()))
        .message(StringUtil.decodeBase64(response.getMessage()))
        .status(response.getStatus())
        .build();
  }

  /**
   * Builds the Judge0 body.
   * 
   * @param code
   * @param input
   * @param expectedOutput
   * @param language
   * @return
   */
  protected Judge0Body buildJudge0Body(
      String code,
      String input,
      String expectedOutput,
      SubmitLanguage language) {
    return Judge0Body.builder()
        .source_code(StringUtil.encodeBase64(code))
        .language_id(language.getCode())
        .stdin(StringUtil.encodeBase64(input))
        .expected_output(StringUtil.encodeBase64(expectedOutput))
        .build();
  }

  /**
   * Builds the Judge0 parameters.
   * 
   * @return
   */
  protected Map<String, String> buildJudge0Params() {
    return Map.of(
        Judge0Configurations.TOKEN, Judge0Configurations.AUTH_TOKEN,
        Judge0Configurations.RETURN_FIELD, Judge0Configurations.FIELD_LIST,
        Judge0Configurations.ENCODED, "true",
        Judge0Configurations.RESULT_WAIT, "true");
  }
}
