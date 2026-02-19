package com.jomi.Util.craftingAction;

import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;

import java.util.ArrayList;
import java.util.List;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Util.ModRoller;

public class BasicCurrencyAction {

    private static final int PREFIX_CAP = 3;
    private static final int SUFFIX_CAP = 3;

    public static void apply(LoadedItem item, Orb orb, List<Omen> omens) {

    if (!item.getItemRarity().equalsIgnoreCase(orb.minRarity())) {
        throw new IllegalStateException(
            "Item rarity " + item.getItemRarity() +
            " does not meet minimum rarity " + orb.minRarity() +
            " for currency " + orb.id()
        );
    }

    if (orb.newRarity() != null &&
        !orb.newRarity().equalsIgnoreCase(item.getItemRarity())) {

        item.setItemRarity(orb.newRarity());
    }


    for (int i = 0; i < orb.removeModifierCount(); i++) {
        removeRandomModifier(item);
    }

    // combine omen effects
    String finalTargetAffix = "BOTH";
    boolean finalTargetTags = false;
    int extraMods = 0;

    if (omens != null) {
        for (Omen omen : omens) {

            // Only apply omens that match the orb
            if (!omen.usedWith().equalsIgnoreCase(orb.id())) continue;

            // Forced prefix/suffix
            if (!omen.targetAffix().equalsIgnoreCase("BOTH")) {
                finalTargetAffix = omen.targetAffix();
            }

            // Tag targeting
            if (omen.targetTags() != null && omen.targetTags()) {
                finalTargetTags = true;
            }

            // Extra modifiers
            extraMods += omen.addExtraModifiers();
        }
    }

    for (int i = 0; i < orb.addedModifierCount(); i++) {
        addModifier(item, finalTargetAffix, finalTargetTags);
    }

    for (int i = 0; i < extraMods; i++) {
        addModifier(item, finalTargetAffix, finalTargetTags);
    }
}


    private static void removeRandomModifier(LoadedItem item) {
        var prefix = item.getPrefix();
        var suffix = item.getSuffix();

        int total = prefix.size() + suffix.size();
        if (total == 0) return;

        int roll = (int)(Math.random() * total);

        if (roll < prefix.size()) {
            prefix.remove(roll);
        } else {
            suffix.remove(roll - prefix.size());
        }
    }



    private static void addModifier(
            LoadedItem item,
            String targetAffix,
            boolean targetTags
    ) {

        ModRoller.ModType type = switch (targetAffix.toUpperCase()) {
            case "PREFIX" -> ModRoller.ModType.PREFIX;
            case "SUFFIX" -> ModRoller.ModType.SUFFIX;
            default -> ModRoller.ModType.BOTH;
        };

        boolean canAddPrefix = item.getPrefix().size() < PREFIX_CAP;
        boolean canAddSuffix = item.getSuffix().size() < SUFFIX_CAP;

        if (!canAddPrefix && !canAddSuffix) {
            System.out.println("no valid modifiers");
            return;
        }

        // Collect item tags from existing mods
        List<String> itemTags = new ArrayList<>();
        item.getPrefix().forEach(rm -> {
            Mod m = ModRegistry.getById(rm.modId());
            if (m != null && m.tags() != null) itemTags.addAll(m.tags());
        });
        item.getSuffix().forEach(rm -> {
            Mod m = ModRegistry.getById(rm.modId());
            if (m != null && m.tags() != null) itemTags.addAll(m.tags());
        });

        for (int attempt = 0; attempt < 50; attempt++) {

            ModRoller.RolledMod rolled = ModRoller.rollRandomTier(
                item.getLoadedItemClass(),
                item.getItemLevel(),
                type
            );

            Mod mod = ModRegistry.getById(rolled.modId());
            if (mod == null) continue;

            // Duplicate blocker
            boolean duplicate =
                item.getPrefix().stream().anyMatch(rm -> rm.modId().equals(mod.id())) ||
                item.getSuffix().stream().anyMatch(rm -> rm.modId().equals(mod.id()));

            if (duplicate) continue;

            // Tag targeting not working correctly
            if (targetTags) {
                var modTags = mod.tags();
                boolean sharesTag = modTags != null && modTags.stream().anyMatch(itemTags::contains);
                if (!sharesTag) continue;
            }

            // PREFIX-ONLY
            if (mod.prefix() && !mod.suffix()) {
                if (canAddPrefix) {
                    item.getPrefix().add(rolled);
                    return;
                }
                continue;
            }

            // SUFFIX-ONLY
            if (mod.suffix() && !mod.prefix()) {
                if (canAddSuffix) {
                    item.getSuffix().add(rolled);
                    return;
                }
                continue;
            }

            // BOTH allowed
            if (mod.prefix() && mod.suffix()) {

                if (canAddPrefix && canAddSuffix) {
                    if (Math.random() < 0.5) {
                        item.getPrefix().add(rolled);
                    } else {
                        item.getSuffix().add(rolled);
                    }
                    return;
                }

                if (canAddPrefix) {
                    item.getPrefix().add(rolled);
                    return;
                }

                if (canAddSuffix) {
                    item.getSuffix().add(rolled);
                    return;
                }
            }
        }

        System.out.println("no valid modifiers");
    }


}
