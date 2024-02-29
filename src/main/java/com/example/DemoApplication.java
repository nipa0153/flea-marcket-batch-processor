package com.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {

		System.exit(SpringApplication.exit(SpringApplication.run(DemoApplication.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {
		long currentTimeMillis = System.currentTimeMillis();

		JobParameters parameters = new JobParametersBuilder()
				.addLong("uniqueParam", currentTimeMillis)
				.toJobParameters();

		jobLauncher.run(job, parameters);
	}

}