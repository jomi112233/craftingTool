package com.jomi.Handlers.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModFile;



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

    public static Map<String, Integer> getModCountPerItemClass() {
        Map<String, Integer> result = new HashMap<>();

        for (var entry : modsByItemClass.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }

        return result;
    }


    


    public static Mod getById(String id) {
        return modsById.get(id);
    }

    public static List<Mod> getItemByClass(String itemClass) {
        return modsByItemClass.getOrDefault(itemClass, List.of());
    }

    public static Collection<Mod> getAll() {
        return modsById.values();
    }

}
