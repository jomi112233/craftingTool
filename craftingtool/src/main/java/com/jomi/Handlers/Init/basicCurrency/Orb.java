package com.jomi.Handlers.Init.basicCurrency;

public record Orb(
    String id,
    String name,
    String minRarity,
    String newRarity,
    int removeModifierCount,
    int addedModifierCount,
    String target,

    String currencyType
) { }