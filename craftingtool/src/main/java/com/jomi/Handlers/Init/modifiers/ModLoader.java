package com.jomi.Handlers.Init.modifiers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Mod> loadDirectory(String rootPath) {
        List<Mod> allMods = new ArrayList<>();
        File root = new File(rootPath);

        walk(root, allMods);

        return allMods;
    }

    private static void walk(File file, List<Mod> allMods) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                walk(f, allMods);
            }
            return;
        }

        String name = file.getName().toLowerCase();
        if (!name.equals("prefix.json") && !name.equals("suffix.json"))
            return;

        try {
            // Load wrapper object
            ModFile wrapper = mapper.readValue(file, ModFile.class);

            for (Mod m : wrapper.mods) {
                m.itemClass = wrapper.itemClass;
                allMods.add(m);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load: " + file.getAbsolutePath(), e);
        }
    }

}
