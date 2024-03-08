package com.example.CategoryBatch.Config.Listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepCompletionNotificationListener implements StepExecutionListener {

    private final Logger log = LogManager.getLogger(StepCompletionNotificationListener.class);

    @SuppressWarnings("null")
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info(stepExecution.getStepName() + ":が完了しました");
        }
        return null;
    }

}
