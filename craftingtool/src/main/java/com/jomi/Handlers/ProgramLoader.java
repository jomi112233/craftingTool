package com.jomi.Handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.omen.OmenFile;
import com.jomi.Handlers.Init.omen.OmenLoader;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Handlers.registry.Omenregistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Util.craftingAction.BasicCurrencyAction;

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

        Path omenPath = root.resolve("craftingMaterial/BaseOmens.json");
        OmenFile omenFile = OmenLoader.loadFromFile(omenPath);
        Omenregistry.registerAll(omenFile);

        long end = System.nanoTime();
        long duration = end - start;

        Map<String, Integer> counts = ModRegistry.getModCountPerItemClass();
        counts.forEach((itemClass, count) ->
            System.out.println(itemClass + ": " + count + " mods")
        );

        System.out.println("Loaded orbs: " + OrbRegistry.size());
        
        System.out.println("Program Loader took: " + duration + " ns");
        System.out.println("Program Loader took: " + duration / 1_000_000 + " ms");

        itemtesting();
    }


    public static void itemtesting() {
        // -----------------------------
        // Create a test item
        // -----------------------------
        LoadedItem item = new LoadedItem();
        item.setItemLevel(75);
        item.setItemRarity("rare");
        item.setLoadedItemClass("weapons/talisman");

        var greater = Omenregistry.get("greater_exaltation");
        var homogenising = Omenregistry.get("homogenising_exaltation");
        var dextral = Omenregistry.get("dextral_exaltation");
        var sinistral = Omenregistry.get("sinistral_exaltation");

        // Build the list (nulls are ignored by apply())
        List<com.jomi.Handlers.Init.omen.Omen> omens = List.of(
            homogenising,
            dextral,
            greater
        );

        List<com.jomi.Handlers.Init.omen.Omen> omensSecondary = List.of(
            sinistral
        );

        // -----------------------------
        // Fetch the base currency (Exalted Orb)
        // -----------------------------
        var exalt = OrbRegistry.get("exalted_orb");

        BasicCurrencyAction.apply(item, exalt, omensSecondary);

        System.out.println("=== ITEM AFTER CRAFTING ===");
        System.out.println("Rarity: " + item.getItemRarity());

        System.out.println("\n--- PREFIXES ---");
        item.getPrefix().forEach(mod -> System.out.println("* " + mod));

        System.out.println("\n--- SUFFIXES ---");
        item.getSuffix().forEach(mod -> System.out.println("* " + mod));

        System.out.println("_______________________________________");



        BasicCurrencyAction.apply(item, exalt, omens);

        // -----------------------------
        // Print results
        // -----------------------------
        System.out.println("=== ITEM AFTER CRAFTING ===");
        System.out.println("Rarity: " + item.getItemRarity());

        System.out.println("\n--- PREFIXES ---");
        item.getPrefix().forEach(mod -> System.out.println("* " + mod));

        System.out.println("\n--- SUFFIXES ---");
        item.getSuffix().forEach(mod -> System.out.println("* " + mod));

        System.out.println("_______________________________________");
    }
}

