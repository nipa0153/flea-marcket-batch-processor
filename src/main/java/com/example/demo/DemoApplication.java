package com.example.demo;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	// @Autowired
	// private JobLauncher jobLauncher;

	public static void main(String[] args) {

		System.exit(SpringApplication.exit(SpringApplication.run(DemoApplication.class,
				args)));
		// SpringApplication.run(DemoApplication.class, args);
	}

}
