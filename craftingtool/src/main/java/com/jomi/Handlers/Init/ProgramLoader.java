package com.jomi.Handlers.Init;

import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Handlers.registry.ModRegistry.ModRegistrySummary;

public class ProgramLoader {

    public void start() {
        long start = System.nanoTime();

        
        List<ModFile> modFiles = ModLoader.loadAll("modifiers/");
        ModRegistrySummary register = ModRegistry.registerAll(modFiles);


        OrbFile orbFile = OrbLoader.load("craftingMaterial/basicCurrency/basicOrbs.json");
        OrbRegistry.registerAll(orbFile);


        long end = System.nanoTime();
        long duration = end - start;








        System.out.println("Registered " + register.itemClassCount() + " items");

        System.out.println("_________________________________");


        Map<String, Integer> counts = ModRegistry.getModCountPerItemClass();
        counts.forEach((itemClass, count) ->
            System.out.println(itemClass + ": " + count + " mods")
        );

        
        System.out.println("Program Loader took: " + duration + " ns");
        System.out.println("Program Loader took: " + duration / 1_000_000 + " ms");
    }
}
