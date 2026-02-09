package com.jomi.Handlers.Init.basicCurrency;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class OrbLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static OrbFile loadFromResources(String resourcePath) {
        try {
            InputStream stream = OrbLoader.class.getClassLoader().getResourceAsStream(resourcePath);

            if (stream == null) {
                throw new RuntimeException("Could not find orb file in resources: " + resourcePath);
            }

            return mapper.readValue(stream, OrbFile.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load orb file from resources: " + resourcePath, e);
        }
    }
}
