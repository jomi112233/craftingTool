package com.jomi.Handlers.Init.modifiers;

import java.util.Map;

public record ModTier( 
    int tier,
    int ilvl,
    int weight,
    Map<String, StatRange> stats
) { }
