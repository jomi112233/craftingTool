package com.jomi.Handlers.Init.basicCurrency;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;

public class OrbLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static OrbFile loadFromFile(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new RuntimeException("Orb file does not exist: " + path);
            }

            return mapper.readValue(path.toFile(), OrbFile.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load orb file from filesystem: " + path, e);
        }
    }

}
