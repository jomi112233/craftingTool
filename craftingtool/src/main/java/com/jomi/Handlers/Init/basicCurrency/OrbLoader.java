package com.jomi.Handlers.Init.basicCurrency;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrbLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static OrbFile load(String path) {
    try {
        InputStream stream = OrbLoader.class.getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            throw new RuntimeException("Could not find orb file in resources: " + path);
        }

        return mapper.readValue(stream, OrbFile.class);

    } catch (Exception e) {
        throw new RuntimeException("Failed to load orb file: " + path, e);
    }
}

}
