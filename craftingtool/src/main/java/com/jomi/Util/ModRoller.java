package com.jomi.Util;

import java.util.Random;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.registry.ModRegistry;

public class ModRoller {

    public record RolledMod(Mod mod, ModTier tier) {

        public int ilvl() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'ilvl'");
        } }

    public enum ModType {
        PREFIX,
        SUFFIX,
        BOTH
    }



    private static final Random random = new Random();

    public static RolledMod rollRandomTier(String itemClass, int itemLevel, ModType type) {

        var mods = ModRegistry.getItemByClass(itemClass);
        if (mods == null || mods.isEmpty()) {
            throw new IllegalArgumentException("No mods registered for itemClass: " + itemClass);
        }

        // Filter mods by prefix/suffix type
        var filteredMods = mods.stream()
            .filter(mod -> switch (type) {
                case PREFIX -> mod.prefix();
                case SUFFIX -> mod.suffix();
                case BOTH   -> true;
            })
            .toList();

        if (filteredMods.isEmpty()) {
            throw new IllegalStateException("No mods of type " + type + " for itemClass " + itemClass);
        }

        // Collect tiers allowed by item level
        var eligibleTiers = filteredMods.stream()
            .flatMap(mod -> mod.tiers().stream())
            .filter(tier -> tier.ilvl() <= itemLevel)
            .toList();

        if (eligibleTiers.isEmpty()) {
            throw new IllegalStateException(
                "No eligible tiers for itemClass " + itemClass +
                " at item level " + itemLevel +
                " for type " + type
            );
        }

        // Compute total weight
        int totalWeight = eligibleTiers.stream()
            .mapToInt(ModTier::weight)
            .sum();

        int roll = random.nextInt(totalWeight);

        // Weighted selection
        for (Mod mod : filteredMods) {
            for (ModTier tier : mod.tiers()) {
                if (tier.ilvl() <= itemLevel) {
                    roll -= tier.weight();
                    if (roll < 0) {
                        return new RolledMod(mod, tier);
                    }
                }
            }
        }


        throw new IllegalStateException("Weighted roll failed for itemClass: " + itemClass);
    }


    
}
