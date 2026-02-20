package com.jomi.Handlers.Init.modifiers;

import java.util.List;

public record ModFile (
    String itemClass,
    List<Mod> mods
) { }
