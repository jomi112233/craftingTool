package com.jomi.Handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Handlers.registry.OrbRegistry;

public class ProgramLoader {

    public static void loadProgramData() {
        long start = System.nanoTime();

        Path root = ConfigLoader.getDataFolder();


        Path modifierPath = root.resolve("modifiers");
        List<ModFile> modFiles = ModLoader.loadAll(modifierPath);
        ModRegistry.registerAll(modFiles);


        Path orbPath = root.resolve("craftingMaterial/basicOrbs.json");
        OrbFile orbFile = OrbLoader.loadFromFile(orbPath);
        OrbRegistry.registerAll(orbFile);



        long end = System.nanoTime();
        long duration = end - start;



        Map<String, Integer> counts = ModRegistry.getModCountPerItemClass();
        counts.forEach((itemClass, count) ->
            System.out.println(itemClass + ": " + count + " mods")
        );

        System.out.println("Loaded orbs: " + OrbRegistry.size());
        
        System.out.println("Program Loader took: " + duration + " ns");
        System.out.println("Program Loader took: " + duration / 1_000_000 + " ms");
    }
}
