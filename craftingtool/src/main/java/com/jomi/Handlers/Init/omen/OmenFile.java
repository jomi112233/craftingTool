package com.jomi.Handlers.Init.omen;

import java.util.List;

public record OmenFile(
    String omenType,
    List<Omen> omens
) { } 
