package com.jomi.Handlers.Init.modifiers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ModFile> loadAll(Path folder) {
        List<ModFile> result = new ArrayList<>();

        try (var stream = Files.walk(folder)) {
            stream
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".json"))
                .forEach(p -> {
                    try {
                        ModFile mf = mapper.readValue(p.toFile(), ModFile.class);
                        result.add(mf);
                    } catch (Exception e) {
                        System.err.println("Failed to parse " + p + ": " + e.getMessage());
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load folder: " + folder, e);
        }

        return result;
    }
}
