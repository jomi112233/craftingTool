package com.jomi.Handlers.Init.modifiers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ModFile> loadAll(String folder) {
    List<ModFile> result = new ArrayList<>();

    try {
        var uri = ModLoader.class.getClassLoader().getResource(folder).toURI();
        Path path = Paths.get(uri);

        try (var stream = Files.walk(path)) {
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
        }

    } catch (Exception e) {
        throw new RuntimeException("Failed to load resources folder: " + folder, e);
    }

    return result;
}

}
