package com.example.demo.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class CategoryStepExecutionListener implements StepExecutionListener {
    private static final Logger log = LogManager.getLogger(CategoryStepExecutionListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("originalsテーブルの読み込みを開始します", stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.debug("originalsテーブルの読み込みが完了しました", stepExecution);
        return null;
    }
}
