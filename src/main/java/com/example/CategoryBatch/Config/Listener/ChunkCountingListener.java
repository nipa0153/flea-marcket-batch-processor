package com.example.CategoryBatch.Config.Listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ChunkCountingListener implements ChunkListener {

    private final Logger log = LogManager.getLogger(ChunkCountingListener.class);

    @SuppressWarnings("null")
    @Override
    public void afterChunk(ChunkContext chunkContext) {
        log.info("Chunk:" + chunkContext.getStepContext().getStepName());
        log.info("読み込み件数:" + chunkContext.getStepContext().getStepExecution().getReadCount());
        log.info("書き出し件数:" + chunkContext.getStepContext().getStepExecution().getWriteCount());
        log.info("処理終了件数:" + chunkContext.getStepContext().getStepExecution().getCommitCount());

    }

    @SuppressWarnings("null")
    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        log.error("itemsテーブル移行処理中にエラーが発生しました:"
                + chunkContext.getStepContext().getStepExecution().getExitStatus().getExitDescription());
    }

}
