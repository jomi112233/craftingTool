package com.jomi.Handlers.Init.basicCurrency;

import com.jomi.Util.NodeShit.searchAction;

public record Orb(
    String id,
    String name,
    String minRarity,
    String newRarity,
    int removeModifierCount,
    int addedModifierCount,
    String target,

    String currencyType
) implements searchAction {

    @Override
    public String getid() {
        return id();
    }

    @Override
    public String getName() {
        return name();
    }
}