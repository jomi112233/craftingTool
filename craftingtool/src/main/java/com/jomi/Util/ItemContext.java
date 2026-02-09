package com.jomi.Util;

import java.util.ArrayList;
import java.util.List;

public class ItemContext {
    public String itemClass;
    public int ilvl;

    public List<ModifierInstance> prefixes = new ArrayList<>();
    public List<ModifierInstance> suffixes = new ArrayList<>();

    public boolean hasMod(String modID){
        return prefixes.stream().anyMatch(m -> m.mod.id.equals(modID)) ||
                suffixes.stream().anyMatch(m -> m.mod.id.equals(modID));
    }
}
