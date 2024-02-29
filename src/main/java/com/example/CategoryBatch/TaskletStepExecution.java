package com.example.CategoryBatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletStepExecution implements StepExecutionListener {

    private static final Logger log = LogManager.getLogger(TaskletStepExecution.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("タスクレット開始");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("タスクレット終了");
        return stepExecution.getExitStatus();
    }
}
