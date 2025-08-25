package com.codepractice.submission_service.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.codepractice.submission_service.model.dto.request.ExecuteRequest;
import com.codepractice.submission_service.model.dto.request.Judge0Body;
import com.codepractice.submission_service.model.dto.request.Judge0Request;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.service.RestTemplateService;
import com.codepractice.submission_service.service.Judge0Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Judge0ServiceImpl extends Judge0Execute implements Judge0Service {

  public Judge0ServiceImpl(RestTemplateService restTemplateService) {
    super(restTemplateService);
  }

  /**
   * Executes the requests.
   * 
   * @param requests
   * @return
   */
  @Override
  public Judge0Response execute(ExecuteRequest request) {
    Map<String, String> params = buildJudge0Params();

    Judge0Body body = buildJudge0Body(
            request.getCode(),
            request.getInput(),
            request.getExpectedOutput(),
            request.getLanguage());

    Judge0Request judge0Request = buildJudge0Request(params, body);

    return sendToJudge0(judge0Request);
  }
}
