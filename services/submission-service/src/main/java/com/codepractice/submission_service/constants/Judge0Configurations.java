package com.codepractice.submission_service.constants;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Judge0Configurations {
    // Judge0 URL
    public static String JUDGE0_URL;
    @Value("${judge0.url}")
    public void setJudge0Url(String value) {
        JUDGE0_URL = value;
    }

    // Configuration settings (instance fields)
    public static int MAX_CPU_TIME_LIMIT;
    @Value("${judge0.max_cpu_time_limit}")
    public void setMaxCpuTimeLimit(int value) {
        MAX_CPU_TIME_LIMIT = value;
    }

    public static int MAX_CPU_EXTRA_TIME;
    @Value("${judge0.max_cpu_extra_time}")
    public void setMaxCpuExtraTime(int value) {
        MAX_CPU_EXTRA_TIME = value;
    }

    public static int MAX_WALL_TIME_LIMIT;
    @Value("${judge0.max_wall_time_limit}")
    public void setMaxWallTimeLimit(int value) {
        MAX_WALL_TIME_LIMIT = value;
    }

    public static int MAX_MEMORY_LIMIT;
    @Value("${judge0.max_memory_limit}")
    public void setMaxMemoryLimit(int value) {
        MAX_MEMORY_LIMIT = value;
    }

    public static int MAX_STACK_LIMIT;
    @Value("${judge0.max_stack_limit}")
    public void setMaxStackLimit(int value) {
        MAX_STACK_LIMIT = value;
    }

    public static int MAX_MAX_PROCESSES_AND_OR_THREADS;
    @Value("${judge0.max_max_processes_and_or_threads}")
    public void setMaxMaxProcessesAndOrThreads(int value) {
        MAX_MAX_PROCESSES_AND_OR_THREADS = value;
    }

    public static boolean ALLOW_ENABLE_PER_PROCESS_AND_THREAD_TIME_LIMIT;
    @Value("${judge0.allow_enable_per_process_and_thread_time_limit}")
    public void setAllowEnablePerProcessAndThreadTimeLimit(boolean value) {
        ALLOW_ENABLE_PER_PROCESS_AND_THREAD_TIME_LIMIT = value;
    }

    public static boolean ALLOW_ENABLE_PER_PROCESS_AND_THREAD_MEMORY_LIMIT;
    @Value("${judge0.allow_enable_per_process_and_thread_memory_limit}")
    public void setAllowEnablePerProcessAndThreadMemoryLimit(boolean value) {
        ALLOW_ENABLE_PER_PROCESS_AND_THREAD_MEMORY_LIMIT = value;
    }

    public static int MAX_MAX_FILE_SIZE;
    @Value("${judge0.max_max_file_size}")
    public void setMaxMaxFileSize(int value) {
        MAX_MAX_FILE_SIZE = value;
    }

    public static int MAX_NUMBER_OF_RUNS;
    @Value("${judge0.max_number_of_runs}")
    public void setMaxNumberOfRuns(int value) {
        MAX_NUMBER_OF_RUNS = value;
    }

    public static String AUTH_TOKEN;
    @Value("${judge0.judge0_auth_token}")
    public void setAuthToken(String value) {
        AUTH_TOKEN = value;
    }

    // Param names
    public static final String TOKEN = "X-Auth-Token";
    public static final String SUBMISSION_TOKEN = "tokens";
    public static final String SOURCE_CODE = "source_code"; // text
    public static final String LANGUAGE_ID = "language_id"; // integer
    public static final String COMPILER_OPTIONS = "compiler_options"; // string (max. 512 chars)
    public static final String COMMAND_LINE_ARGUMENTS = "command_line_arguments"; // string (max. 512 chars)
    public static final String STDIN = "stdin"; // text
    public static final String EXPECTED_OUTPUT = "expected_output"; // text
    public static final String CPU_TIME_LIMIT = "cpu_time_limit"; // float
    public static final String CPU_EXTRA_TIME = "cpu_extra_time"; // float
    public static final String WALL_TIME_LIMIT = "wall_time_limit"; // float
    public static final String MEMORY_LIMIT = "memory_limit"; // float
    public static final String STACK_LIMIT = "stack_limit"; // integer
    public static final String MAX_PROCESSES_AND_OR_THREADS = "max_processes_and_or_threads"; // integer
    public static final String ENABLE_PER_PROCESS_AND_THREAD_TIME_LIMIT = "enable_per_process_and_thread_time_limit"; // boolean
    public static final String ENABLE_PER_PROCESS_AND_THREAD_MEMORY_LIMIT = "enable_per_process_and_thread_memory_limit"; // boolean
    public static final String MAX_FILE_SIZE = "max_file_size"; // integer
    public static final String REDIRECT_STDERR_TO_STDOUT = "redirect_stderr_to_stdout"; // boolean
    public static final String ENABLE_NETWORK = "enable_network"; // boolean
    public static final String NUMBER_OF_RUNS = "number_of_runs"; // integer
    public static final String ADDITIONAL_FILES = "additional_files"; // Base64 Encoded String
    public static final String CALLBACK_URL = "callback_url"; // string
    public static final String STDOUT = "stdout"; // text
    public static final String STDERR = "stderr"; // text
    public static final String COMPILE_OUTPUT = "compile_output"; // text
    public static final String MESSAGE = "message"; // text
    public static final String EXIT_CODE = "exit_code"; // integer
    public static final String EXIT_SIGNAL = "exit_signal"; // integer
    public static final String STATUS = "status"; // object
    public static final String CREATED_AT = "created_at"; // datetime
    public static final String FINISHED_AT = "finished_at"; // datetime
    public static final String TIME = "time"; // float
    public static final String WALL_TIME = "wall_time"; // float
    public static final String MEMORY = "memory"; // float
    public static final String RESULT_WAIT = "wait"; // boolean
    public static final String ENCODED = "base64_encoded"; // boolean
    public static final String RETURN_FIELD = "fields"; //string
    public static final String PAGE = "page"; //string
    public static final String PER_PAGE = "per_page"; //string
    public static final String FIELD_LIST = String.join(",", List.of(
        "token",
        "stdout",
        "stderr",
        "status_id",
        "language_id",
        "time",
        "memory"
    ));
}
