package com.codepractice.submission_service.constants;

import lombok.Getter;

@Getter
public final class Judge0Endpoint {
    private static final String SUBMISSION = "submissions";
    private static final String BATCH = "batch";
    private static final String SUBMISSION_BATCH = String.join("/", SUBMISSION, BATCH);
    private static final String LANGUAGE = "languages";
    private static final String STATUS = "statuses";
    private static final String SYSTEM_INFO = "system_info";
    private static final String CONFIG_INFO = "config_info";
    private static final String STATISTIC = "statistics";
    private static final String HEALTH = "workers";

    public static final String SUBMISSION_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, SUBMISSION);
    public static final String SUBMISSION_BATCH_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, SUBMISSION_BATCH);
    public static final String LANGUAGE_ENPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, LANGUAGE);
    public static final String STATUS_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, STATUS);
    public static final String SYSTEM_INFO_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, SYSTEM_INFO);
    public static final String CONFIG_INFO_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, CONFIG_INFO);
    public static final String STATISTIC_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, STATISTIC);
    public static final String HEALTH_ENDPOINT = String.join("/", Judge0Configurations.JUDGE0_URL, HEALTH);
}
