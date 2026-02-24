package com.jomi.Util;

import java.util.List;
import java.util.Random;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.registry.ModRegistry;

public class ModRoller {

    // ------------------------------------------------------------
    // RolledMod: represents a chosen tier of a mod (no stat rolling)
    // ------------------------------------------------------------
    public record RolledMod(
        String modId,
        String name,
        List<String> tags,
        ModTier tier
    ) {

        public RolledMod {
            tags = List.copyOf(tags);
        }

        public int ilvl() {
            return tier.ilvl();
        }

        @Override
        public String toString() {
            return "RolledMod{ id=" + modId +
                ", name=" + name +
                ", tags=" + tags +
                ", tier=" + tier.tier() +
                ", stats=" + tier.stats() +
                " }";
        }
    }


    // ------------------------------------------------------------
    // ModType: used for random rolling
    // ------------------------------------------------------------
    public enum ModType {
        PREFIX,
        SUFFIX,
        BOTH
    }

    private static final Random random = new Random();


    // ------------------------------------------------------------
    // NEW: Create a RolledMod from a Mod definition + item level
    // ------------------------------------------------------------
    public static RolledMod fromMod(Mod mod, int itemLevel) {

        // Pick the highest tier allowed by item level
        ModTier tier = mod.tiers().stream()
            .filter(t -> t.ilvl() <= itemLevel)
            .sorted((a, b) -> Integer.compare(b.tier(), a.tier())) // highest tier first
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No valid tier for mod " + mod.id() + " at ilvl " + itemLevel
            ));

        return new RolledMod(
            mod.id(),
            mod.name(),
            mod.tags(),
            tier
        );
    }


    // ------------------------------------------------------------
    // Existing: Weighted random mod roller
    // ------------------------------------------------------------
    public static RolledMod rollRandomTier(String itemClass, int itemLevel, ModType type) {

        var mods = ModRegistry.getItemByClass(itemClass);
        if (mods == null || mods.isEmpty()) {
            throw new IllegalArgumentException("No mods registered for itemClass: " + itemClass);
        }

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

        record Entry (Mod mod, ModTier tier) { }

        var entries = filteredMods.stream()
            .flatMap(mod -> mod.tiers().stream()
                .filter(tier -> tier.ilvl() <= itemLevel)
                .map(tier -> new Entry(mod, tier)))
            .toList();

        if (entries.isEmpty()) {
            throw new IllegalStateException(
                "No eligible tiers for itemClass " + itemClass +
                " at item level " + itemLevel +
                " for type " + type
            );
        }

        int totalWeight = entries.stream()
            .mapToInt(e -> e.tier.weight())
            .sum();

        int roll = random.nextInt(totalWeight);

        for (Entry e : entries) {
            roll -= e.tier.weight();
            if (roll < 0) {
                return new RolledMod(
                    e.mod.id(),
                    e.mod.name(),
                    e.mod.tags(),
                    e.tier
                );
            }
        }

        throw new IllegalStateException("Weighted roll failed for itemClass: " + itemClass);
    }
}
