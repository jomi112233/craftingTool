package com.jomi.Handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


public class ConfigLoader {
    
    private static Path dataFolder;

    public static void loadConfig() {
        try (var in = Files.newInputStream(Path.of("config.properties"))) {

            Properties properties = new Properties();
            properties.load(in);

            String folder = properties.getProperty("dataFolder");
            if (folder == null || folder.isBlank()) {
                throw new RuntimeException("dataFolder not set in config.properties");
            }

            dataFolder = Path.of(folder);
            System.out.println("using data folder: " + dataFolder);

        } catch (IOException e) {
            throw new RuntimeException("failed to load config.properties", e);
        }
    }


    public static Path getDataFolder() {
        return dataFolder;
    }
}
