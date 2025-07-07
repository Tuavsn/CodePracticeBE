package com.codepractice.submission_service.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codepractice.submission_service.model.dto.internal.request.Judge0Request;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionReponse;
import com.codepractice.submission_service.service.SubmitStrategy;

@Service
public class SynchronousSubmit implements SubmitStrategy {

    @Override
    public List<SubmissionReponse> execute(List<Judge0Request> requests, Map<String, String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
