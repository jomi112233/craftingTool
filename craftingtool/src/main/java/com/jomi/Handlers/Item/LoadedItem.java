package com.jomi.Handlers.Item;

import java.util.List;

import com.jomi.Util.ModRoller;

public record LoadedItem(
    int itemLevel,
    String itemRarity,
    String loadedItemClass,

    List<ModRoller.RolledMod> prefix,
    List<ModRoller.RolledMod> suffix
) { } 
