package com.jomi.Handlers.Init.modifiers;

import java.util.List;

import com.jomi.Util.NodeShit.searchAction;

public record Mod(
    String id,
    boolean prefix,
    boolean suffix,
    String name,
    List<String> tags,
    List<ModTier> tiers
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
