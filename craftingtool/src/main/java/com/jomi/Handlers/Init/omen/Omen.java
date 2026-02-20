package com.jomi.Handlers.Init.omen;

public record Omen(
    String id,
    String name,
    String usedWith,
    String usedWithCurrencyType,
    String targetAffix,
    Boolean targetTags,
    int addExtraModifiers
) { } 
