package com.example.CategoryBatch.Config.Processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.DTO.ItemsDto;
import com.example.Domain.Items;

@Component
public class ItemCategoryCheckProcessor implements ItemProcessor<ItemsDto, Items> {

    private final Logger log = LogManager.getLogger(ItemCategoryCheckProcessor.class);
    private MakeCsvFile makeCsvFile = new MakeCsvFile();

    @SuppressWarnings("null")
    @Override
    public Items process(ItemsDto itemsDto) throws Exception {
        Items items = new Items();
        if (itemsDto.getCategory().equals(0)) {
            makeCsvFile.saveAsCsv(itemsDto,
                    "/Users/sakaidashigeaki/src/github.com/ShigeakiSakaida/flea-marcket-batch-processor/flea-marcket-batch-processor/error_data/error.csv");
            log.info(" category は『 null 』 です id: " + itemsDto.getId() + " を除外して『error.csv』保管しました");
        } else {
            items.setName(itemsDto.getName());
            items.setConditionId(itemsDto.getConditionId());
            items.setCategory(itemsDto.getCategory());
            items.setBrand(itemsDto.getBrand());
            items.setPrice(itemsDto.getPrice());
            items.setShipping(itemsDto.getShipping());
            items.setDescription(itemsDto.getDescription());
        }
        return items;
    }

}
