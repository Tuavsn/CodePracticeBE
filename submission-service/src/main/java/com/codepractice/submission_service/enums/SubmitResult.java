package com.codepractice.submission_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmitResult {
    IN_QUEUE(1, "In Queue", "Waiting in the queue"),
    PROCESSING(2, "Processing", "Currently processing"),
    ACCEPTED(3, "Accepted", "✅ Correct answer"),
    WRONG_ANSWER(4, "Wrong Answer", "❌ Incorrect output"),
    TIME_LIMIT_EXCEEDED(5, "Time Limit Exceeded", "⏰ Execution timed out"),
    COMPILATION_ERROR(6, "Compilation Error", "💥 Code failed to compile"),
    RUNTIME_ERROR_SIGSEGV(7, "Runtime Error (SIGSEGV)", "💣 Segmentation fault"),
    RUNTIME_ERROR_SIGXFSZ(8, "Runtime Error (SIGXFSZ)", "💣 Output file too large"),
    RUNTIME_ERROR_SIGFPE(9, "Runtime Error (SIGFPE)", "💣 Division by zero or math error"),
    RUNTIME_ERROR_SIGABRT(10, "Runtime Error (SIGABRT)", "💣 Process aborted"),
    RUNTIME_ERROR_NZEC(11, "Runtime Error (NZEC)", "💣 Non-zero exit code"),
    RUNTIME_ERROR_OTHER(12, "Runtime Error (Other)", "💣 Unknown runtime error"),
    INTERNAL_ERROR(13, "Internal Error", "💀 Internal Judge0 error"),
    EXEC_FORMAT_ERROR(14, "Exec Format Error", "⚙️ Invalid executable format");

    private final int code;
    private final String name;
    private final String displayName;
}
