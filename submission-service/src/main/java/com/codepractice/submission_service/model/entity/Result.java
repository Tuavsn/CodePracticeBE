package com.codepractice.submission_service.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.codepractice.submission_service.enums.SubmitResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Result extends BaseEntity {
    private String submissionId;
    private SubmitResult result;
    private String token;
    private String error;
    private String stdout;
    private double time;
    private double memory;
    private double point;
}
