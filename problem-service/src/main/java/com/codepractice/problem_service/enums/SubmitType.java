package com.codepractice.problem_service.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmitType {
    SYNCHRONOUS("synchronous", "Synchronous"),
    ASYNCHRONOUS("asynchronous", "Asynchronous");

    private final String name;
    private final String displayName;

    public static SubmitType getByName(String name) {
        return Arrays.stream(values())
            .filter(result -> result.name.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
