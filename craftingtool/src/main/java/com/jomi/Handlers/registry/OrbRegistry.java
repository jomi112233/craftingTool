package com.jomi.Handlers.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.basicCurrency.OrbFile;

public class OrbRegistry {
    private static final Map<String, Orb> ORBS = new HashMap<>();

    public static void registerAll(OrbFile file) {
        for (Orb orb : file.orbs()) {
            ORBS.put(orb.id(), orb);
        }
    }

    public static Orb get(String id) {
        return ORBS.get(id);
    }


    public static int size() { 
        return ORBS.size(); 
    }

    public static void clear() {
        ORBS.clear();
    }

    public static Collection<Orb> getAll() {
        return ORBS.values();
    }

}
