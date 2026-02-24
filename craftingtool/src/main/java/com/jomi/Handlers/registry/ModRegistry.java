package com.jomi.Handlers.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModTier;



public class ModRegistry {
    private static final Map<String, Mod> modsById = new HashMap<>();
    private static final Map<String, List<Mod>> modsByItemClass = new HashMap<>();

    public record ModRegistrySummary(int modCount, int itemClassCount) { }

    public static ModRegistrySummary registerAll(List<ModFile> files) {
        for (ModFile mf : files) {
            for (Mod mod : mf.mods()) {

                modsById.put(mod.id(), mod);

                modsByItemClass
                    .computeIfAbsent(mf.itemClass(), k -> new ArrayList<>())
                    .add(mod);
            }
        }

        return new ModRegistrySummary(
            modsById.size(),
            modsByItemClass.size()
        );
    }

    public static void register(String itemClass, List<Mod> mods) {
        for (Mod mod : mods) {
            modsById.put(mod.id(), mod);

            modsByItemClass
                .computeIfAbsent(itemClass, k -> new ArrayList<>())
                .add(mod);
        }
    }

    public static Map<String, Integer> getModCountPerItemClass() {
        Map<String, Integer> result = new HashMap<>();

        for (var entry : modsByItemClass.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }

        return result;
    }

    public static List<String> getAllItemClasses() {
        return new ArrayList<>(modsByItemClass.keySet());
    }


    public static Mod getById(String id) {
        return modsById.get(id);
    }

    public static List<Mod> getItemByClass(String itemClass) {
        return modsByItemClass.getOrDefault(itemClass, List.of());
    }

    public static List<Mod> searchByTag(String tag) {
        return modsById.values().stream()
            .filter(m -> m.tags() != null && Arrays.asList(m.tags()).contains(tag))
            .toList();
    }

    public static List<Mod> searchByName(String query) {
        String q = query.toLowerCase();
        return modsById.values().stream()
            .filter(m -> m.name().toLowerCase().contains(q))
            .toList();
    }

    public static int getTotalWeightForItemClass(String itemClass) {
        var mods = modsByItemClass.get(itemClass);
        if (mods == null) {
            return 0;
        }

        return mods.stream()
            .flatMap(mod -> mod.tiers().stream())
            .mapToInt(ModTier::weight)
            .sum();
    }

    public static int getTotalWeight(Mod mod) {
        return mod.tiers().stream()
            .mapToInt(ModTier::weight)
            .sum();
    }

    public static Collection<Mod> getAll() {
        return modsById.values();
    }

    public static void clear() {
        modsById.clear();
        modsByItemClass.clear();
    }

}
