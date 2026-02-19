package com.jomi.Handlers.registry;

import java.util.HashMap;
import java.util.Map;

import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.omen.OmenFile;

public class Omenregistry {
    private static final Map<String, Omen> OMENS = new HashMap<>();

    public static void registerAll(OmenFile file) {
        for (Omen omen : file.omens()) {
            OMENS.put(omen.id(), omen);
        }
    }

    public static Omen get(String id) {
        return OMENS.get(id);
    }

    public static int size() {
        return OMENS.size();
    }

    public static void clear() {
        OMENS.clear();
    }
    
}
