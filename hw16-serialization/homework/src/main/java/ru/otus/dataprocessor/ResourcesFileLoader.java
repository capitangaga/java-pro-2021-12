package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

public class ResourcesFileLoader implements Loader {

    private final String fileToLoad;

    public ResourcesFileLoader(String fileName) {
        this.fileToLoad = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileToLoad);
             JsonParser parser = Json.createParser(is))
        {
            List<Measurement> result = new ArrayList<>();
            while (parser.hasNext()) {
                if (parser.next() == JsonParser.Event.START_OBJECT) {
                    JsonObject measurementObject = parser.getObject();
                    try {
                        result.add(new Measurement(
                                measurementObject.getString("name"),
                                measurementObject.getJsonNumber("value").doubleValue()));
                    } catch (Exception ex) {
                        throw new FileProcessException("Bad measurement object: " + measurementObject);
                    }
                }
            }
            return result;
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
