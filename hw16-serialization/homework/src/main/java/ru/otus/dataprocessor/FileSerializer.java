package ru.otus.dataprocessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class FileSerializer implements Serializer {
    private final String fileToWrite;

    public FileSerializer(String fileName) {
        this.fileToWrite = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var entriesToWrite = data.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
        try (FileOutputStream os = new FileOutputStream(fileToWrite);
             JsonWriter writer = Json.createWriter(os))
        {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            entriesToWrite.forEach(entry -> objectBuilder.add(entry.getKey(), entry.getValue()));
            writer.write(objectBuilder.build());
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
    }
}
