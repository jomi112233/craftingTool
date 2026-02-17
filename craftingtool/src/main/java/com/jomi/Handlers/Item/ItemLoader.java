package com.jomi.Handlers.Item;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ItemLoader {

    private static final ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    public static LoadedItem loadFromJson(Path path) {
        try {
            return mapper.readValue(path.toFile(), LoadedItem.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load item from: " + path, e);
        }
    }

    public static void saveToJson(LoadedItem item, Path path) {
        try {
            mapper.writeValue(path.toFile(), item);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save item to: " + path, e);
        }
    }
}
