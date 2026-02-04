package com.jomi.Handlers.Init;

import java.util.List;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegstry;
import com.jomi.Handlers.registry.OrbRegistry;

public class ProgramLoader {
    public void start(){
        long start = System.nanoTime();


        List<Mod> mods = ModLoader.loadDirectory("modifiers");
        ModRegstry.registerAll(mods);

        OrbFile file = OrbLoader.load("craftingMaterial/basicCurrency/basicOrbs.json");
        OrbRegistry.registerAll(file);





        long end = System.nanoTime();
        long duration = end - start;

        System.out.println("Program Loader took: " + duration + " ns");
        System.out.println("Program Loader took: " + duration / 1000000 + " ms");
    }
}
