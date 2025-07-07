package com.codepractice.submission_service.model.dto.internal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Judge0Response {
    private int language_id;
    private String stdout;
    private int status_id;
    private String stderr;
    private String token;
    private double time;
    private double memory;
}
