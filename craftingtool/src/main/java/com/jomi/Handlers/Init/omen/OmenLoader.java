package com.jomi.Handlers.Init.omen;

import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OmenLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static OmenFile loadFromFile(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new RuntimeException("Omen file does not exist: " + path);
            }

            return mapper.readValue(path.toFile(), OmenFile.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load Omen File from file system: " + path);
        }
    }
}
