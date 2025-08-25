package com.codepractice.submission_service.constants;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Judge0Configurations {
  // Judge0 URL
  public static String JUDGE0_ENDPOINT;

  @Value("${judge0.url}")
  public void setJudge0Url(String value) {
    JUDGE0_ENDPOINT = value;
  }

  // Callback URL
  public static String CALLBACK_ENDPOINT;

  @Value("${judge0.callback_url}")
  public void setCallbackUrl(String value) {
    CALLBACK_ENDPOINT = value;
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
  public static final String SUBMISSION = "submissions";
  public static final String BATCH = "batch";
  public static final String SUBMISSION_BATCH = String.join("/", SUBMISSION, BATCH);
  public static final String LANGUAGE = "languages";
  public static final String STATUSES = "statuses";
  public static final String SYSTEM_INFO = "system_info";
  public static final String CONFIG_INFO = "config_info";
  public static final String STATISTIC = "statistics";
  public static final String HEALTH = "workers";
  public static final String TOKEN = "X-Auth-Token";
  public static final String SUBMISSION_TOKEN = "tokens";
  public static final String SOURCE_CODE = "source_code";
  public static final String LANGUAGE_ID = "language_id";
  public static final String COMPILER_OPTIONS = "compiler_options";
  public static final String COMMAND_LINE_ARGUMENTS = "command_line_arguments";
  public static final String STDIN = "stdin";
  public static final String EXPECTED_OUTPUT = "expected_output";
  public static final String CPU_TIME_LIMIT = "cpu_time_limit";
  public static final String CPU_EXTRA_TIME = "cpu_extra_time";
  public static final String WALL_TIME_LIMIT = "wall_time_limit";
  public static final String MEMORY_LIMIT = "memory_limit";
  public static final String STACK_LIMIT = "stack_limit";
  public static final String MAX_PROCESSES_AND_OR_THREADS = "max_processes_and_or_threads";
  public static final String ENABLE_PER_PROCESS_AND_THREAD_TIME_LIMIT = "enable_per_process_and_thread_time_limit";
  public static final String ENABLE_PER_PROCESS_AND_THREAD_MEMORY_LIMIT = "enable_per_process_and_thread_memory_limit";
  public static final String MAX_FILE_SIZE = "max_file_size";
  public static final String REDIRECT_STDERR_TO_STDOUT = "redirect_stderr_to_stdout";
  public static final String ENABLE_NETWORK = "enable_network";
  public static final String NUMBER_OF_RUNS = "number_of_runs";
  public static final String ADDITIONAL_FILES = "additional_files";
  public static final String CALLBACK_URL = "callback_url";
  public static final String STDOUT = "stdout";
  public static final String STDERR = "stderr";
  public static final String COMPILE_OUTPUT = "compile_output";
  public static final String MESSAGE = "message";
  public static final String EXIT_CODE = "exit_code";
  public static final String EXIT_SIGNAL = "exit_signal";
  public static final String STATUS = "status";
  public static final String CREATED_AT = "created_at";
  public static final String FINISHED_AT = "finished_at";
  public static final String TIME = "time";
  public static final String WALL_TIME = "wall_time";
  public static final String MEMORY = "memory";
  public static final String RESULT_WAIT = "wait";
  public static final String ENCODED = "base64_encoded";
  public static final String RETURN_FIELD = "fields";
  public static final String PAGE = "page";
  public static final String PER_PAGE = "per_page";
  public static final String FIELD_LIST = String.join(",", List.of(
      "token",
      "stdout",
      "stderr",
      "status",
      "message",
      "compile_output",
      "language_id",
      "time",
      "memory"));

  // Endpoint
  public static String getSubmissionEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, SUBMISSION);
  }

  public static String getSubmissionBatchEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, SUBMISSION_BATCH);
  }

  public static String getLanguageEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, LANGUAGE);
  }

  public static String getStatusEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, STATUSES);
  }

  public static String getSystemInfoEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, SYSTEM_INFO);
  }

  public static String getConfigInfoEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, CONFIG_INFO);
  }

  public static String getStatisticEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, STATISTIC);
  }

  public static String getHealthEndpoint() {
    return String.join("/", Judge0Configurations.JUDGE0_ENDPOINT, HEALTH);
  }
}
