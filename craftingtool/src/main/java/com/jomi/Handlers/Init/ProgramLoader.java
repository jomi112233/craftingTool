package com.jomi.Handlers.Init;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegistry;

public class ProgramLoader {

    public void start() {
        long start = System.nanoTime();

        
        List<ModFile> modFiles = ModLoader.loadAll(Path.of("data/modifiers"));
        ModRegistry.registerAll(modFiles);



        Path orbPath = Paths.get("data/craftingMaterial/basicOrbs.json");
        OrbLoader.loadFromFile(orbPath);



        long end = System.nanoTime();
        long duration = end - start;








        Map<String, Integer> counts = ModRegistry.getModCountPerItemClass();
        counts.forEach((itemClass, count) ->
            System.out.println(itemClass + ": " + count + " mods")
        );

        
        System.out.println("Program Loader took: " + duration + " ns");
        System.out.println("Program Loader took: " + duration / 1_000_000 + " ms");
    }
}
