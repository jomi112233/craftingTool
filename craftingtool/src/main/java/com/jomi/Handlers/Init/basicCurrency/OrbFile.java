package com.jomi.Handlers.Init.basicCurrency;

import java.util.List;

public record OrbFile(String currencyType, List<Orb> orbs) {
    public OrbFile { orbs = List.copyOf(orbs); }
} 

