package com.codepractice.problem_service.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmitResult {
    IN_QUEUE(0, "in_queue", "In Queue"),
    PROCESSING(1, "processing", "Processing"),
    ACCEPTED(2, "accepted", "Accepted"),
    WRONG_ANSWER(3, "wrong_answer", "Wrong Answer"),
    TIME_LIMIT(4, "time_limit_exceeded", "Time Limit Exceeded"),
    COMPILE_ERROR(5, "compilation_error", "Compliation Error");

    private final int code;
    private final String name;
    private final String displayName;

    public static String getDisplayNameByCode(int code) {
        return Arrays.stream(values())
                .filter(result -> result.getCode() == code)
                .map(SubmitResult::getDisplayName)
                .findFirst()
                .orElse("Unknown Code"); // Trả về "Unknown Code" nếu không tìm thấy
    }
}
