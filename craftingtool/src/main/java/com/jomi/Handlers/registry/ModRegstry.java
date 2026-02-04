package com.jomi.Handlers.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jomi.Handlers.Init.modifiers.Mod;

public class ModRegstry {
    private static final Map<String, Mod> MODS = new HashMap<>();
    public static void registerAll(List<Mod> mods){
        for(Mod mod : mods){
            MODS.put(mod.itemClass + ":" + mod.id, mod);
        }
    }

    public static Mod get(String itemClass, String id){
        return MODS.get(itemClass + ":" + id);
    }

    public static Collection<Mod> all(){
        return MODS.values();
    }
}
