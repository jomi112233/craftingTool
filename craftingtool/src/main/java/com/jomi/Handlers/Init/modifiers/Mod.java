package com.jomi.Handlers.Init.modifiers;

import java.util.List;

public record Mod(
    String id,
    boolean prefix,
    boolean suffix,
    String name,
    List<String> tags,
    List<ModTier> tiers
) { }
