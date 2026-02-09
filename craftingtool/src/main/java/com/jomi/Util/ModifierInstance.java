package com.jomi.Util;

import java.util.HashMap;
import java.util.Map;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;

public class ModifierInstance {
    public Mod mod;
    public ModTier tier;
    public Map<String, Double> rolledValues = new HashMap<>();
}
