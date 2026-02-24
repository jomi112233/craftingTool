package com.jomi.Handlers.Init.omen;

import com.jomi.Util.NodeShit.searchAction;

public record Omen(
    String id,
    String name,
    String usedWith,
    String usedWithCurrencyType,
    String targetAffix,
    Boolean targetTags,
    int addExtraModifiers
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
