package com.codepractice.common_lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.codepractice")
public class CommonLibApplication {
  
  public static void main(String[] args) {
		SpringApplication.run(CommonLibApplication.class, args);
	}

}
