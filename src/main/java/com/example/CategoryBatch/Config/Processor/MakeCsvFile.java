package com.example.CategoryBatch.Config.Processor;

import java.io.FileWriter;
import java.io.IOException;

import com.example.DTO.ItemsDto;

public class MakeCsvFile {

    // CSV形式に変換
    public void saveAsCsv(ItemsDto itemsDto, String filePath) {
        String csvData = toCsv(itemsDto);
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            fileWriter.write(csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CSV化の処理
    private String toCsv(ItemsDto itemDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(itemDto.getName()).append(",")
                .append(itemDto.getBrand()).append(",")
                .append(itemDto.getCategory()).append(",")
                .append(itemDto.getConditionId()).append(",")
                .append(itemDto.getPrice()).append(",")
                .append(itemDto.getShipping()).append(",")
                .append(itemDto.getDescription()).append("\n");

        return stringBuilder.toString();
    }

}
