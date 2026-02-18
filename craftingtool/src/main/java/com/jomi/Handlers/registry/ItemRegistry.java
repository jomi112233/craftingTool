package com.jomi.Handlers.registry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jomi.Handlers.Item.LoadedItem;

public class ItemRegistry {

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

    public static LoadedItem createNewItem(Path projectFolder,
                                       int itemLevel,
                                       String itemRarity,
                                       String loadedItemClass) {
        try {
            Files.createDirectories(projectFolder);
            Files.createDirectories(projectFolder.resolve("itemruns"));

            LoadedItem item = new LoadedItem();
            item.setItemLevel(itemLevel);
            item.setItemRarity(itemRarity);
            item.setLoadedItemClass(loadedItemClass);

            saveToJson(item, projectFolder.resolve("baseitem.json"));
            saveToJson(item, projectFolder.resolve("baseitemCompleted.json"));

            return item;

        } catch (IOException e) {
            throw new RuntimeException("Failed to create new item in: " + projectFolder, e);
        }
    }





}
