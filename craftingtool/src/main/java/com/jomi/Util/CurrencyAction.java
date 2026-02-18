package com.jomi.Util;

import java.util.ArrayList;
import java.util.List;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.OrbRegistry;

public class CurrencyAction {
    /*
    public static LoadedItem apply(
        LoadedItem item,
        String orbId
    ) {
        // 1. Find the orb
        Orb orb = OrbRegistry.get(orbId); 
        if (orb == null) { 
            throw new IllegalArgumentException("Unknown orb: " + orbId); 
        }

        // 2. Check rarity requirement
        if (!rarityAllowed(item.getItemRarity(), orb.minRarity())) {
            throw new IllegalStateException("Item rarity too low for: " + orb.name());
        }

        // 3. Determine new rarity
        String newRarity = orb.newRarity().equals("any")
            ? item.getItemRarity()
            : orb.newRarity();

        // 4. Copy existing mods
        List<ModRoller.RolledMod> prefixes = new ArrayList<>(item.prefix());
        List<ModRoller.RolledMod> suffixes = new ArrayList<>(item.suffix());

        // 5. Remove mods
        for (int i = 0; i < orb.removeModifierCount(); i++) {
            removeRandomMod(prefixes, suffixes);
        }

        // 6. Add mods (respecting limits)
        for (int i = 0; i < orb.addedModifierCount(); i++) {
            addModWithLimits(item, newRarity, orb.target(), prefixes, suffixes);
        }

        // 7. Return updated item
        return new LoadedItem(
            item.getItemLevel(),
            newRarity,
            item.getLoadedItemClass(),
            prefixes,
            suffixes
        );
    }


    private static int maxPrefixes(String rarity) {
        return switch (rarity) {
            case "normal" -> 0;
            case "magic"  -> 1;
            case "rare"   -> 3;
            default -> throw new IllegalArgumentException("Unknown rarity: " + rarity);
        };
    }

    private static int maxSuffixes(String rarity) {
        return switch (rarity) {
            case "normal" -> 0;
            case "magic"  -> 1;
            case "rare"   -> 3;
            default -> throw new IllegalArgumentException("Unknown rarity: " + rarity);
        };
    }

    private static boolean rarityAllowed(String itemRarity, String minRarity) {
        List<String> order = List.of("normal", "magic", "rare");
        return order.indexOf(itemRarity) >= order.indexOf(minRarity);
    }

    private static void removeRandomMod(
        List<ModRoller.RolledMod> prefixes,
        List<ModRoller.RolledMod> suffixes
    ) {
        List<List<ModRoller.RolledMod>> pools = List.of(prefixes, suffixes);
        var nonEmpty = pools.stream().filter(p -> !p.isEmpty()).toList();
        if (nonEmpty.isEmpty()) {
            return;
        } 

        var pool = nonEmpty.get((int) (Math.random() * nonEmpty.size()));
        pool.remove((int) (Math.random() * pool.size()));
    }

    private static void addModWithLimits(
        LoadedItem item,
        String rarity,
        String target,
        List<ModRoller.RolledMod> prefixes,
        List<ModRoller.RolledMod> suffixes
    ) {
        int maxP = maxPrefixes(rarity);
        int maxS = maxSuffixes(rarity);

        boolean canAddPrefix = prefixes.size() < maxP;
        boolean canAddSuffix = suffixes.size() < maxS;

        if (!canAddPrefix && !canAddSuffix) {
            return;
        }

        switch (target) {
            case "PREFIX" -> {
                if (canAddPrefix) {
                    prefixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.PREFIX));
                }
            }
            case "SUFFIX" -> {
                if (canAddSuffix) {
                    suffixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.SUFFIX));
                }
            }
            case "BOTH" -> {
                if (canAddPrefix && !canAddSuffix) {
                    prefixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.PREFIX));
                } else if (!canAddPrefix && canAddSuffix) {
                    suffixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.SUFFIX));
                } else {
                    boolean rollPrefix = Math.random() < 0.5;
                    if (rollPrefix) {
                        prefixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.PREFIX));
                    } else {
                        suffixes.add(ModRoller.rollRandomTier(item.loadedItemClass(), item.itemLevel(), ModRoller.ModType.SUFFIX));
                    }
                }
            }
        }
    }
    */
}
