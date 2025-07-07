package com.codepractice.submission_service.service.impl;

import org.springframework.stereotype.Component;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.service.SubmitStrategy;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SubmissionFactory {
    private final AsynchronousSubmit asynchronousSubmit;
    private final SynchronousSubmit synchronousSubmit;

    public SubmitStrategy getSubmitStrategy(ExecuteType type) {
        switch (type) {
            case RUN:
                return synchronousSubmit;
            case SUBMIT:
                return asynchronousSubmit;
            default:
                throw new AppException(ErrorCode.UNSUPPORTED_EXECUTE_TYPE);
        }
    }
}
