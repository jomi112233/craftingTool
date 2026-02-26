package com.jomi.Util.craftingAction;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller.RolledMod;

public class iterationAction {


    public record BinomialResult(
    double perRollChance,
    int rolls25,
    int rolls50,
    int rolls75
    ) { }


    public static BinomialResult calculateBinomial(
            LoadedItem item,
            Orb orb,
            Omen omen,
            RolledMod target
            
    ) {

        int itemIlvl = item.getItemLevel();
        int minModLevel = orb.minModifierLevel();

        // Determine affix pool
        boolean allowPrefix = omen.targetAffix().equalsIgnoreCase("PREFIX")
                || omen.targetAffix().equalsIgnoreCase("BOTH");

        boolean allowSuffix = omen.targetAffix().equalsIgnoreCase("SUFFIX")
                || omen.targetAffix().equalsIgnoreCase("BOTH");

        int totalWeight = 0;
        int successWeight = 0;

        // Loop through all mods
        for (Mod mod : ModRegistry.getAll()) {

            // Affix filtering
            if (mod.prefix() && !allowPrefix) continue;
            if (mod.suffix() && !allowSuffix) continue;

            for (ModTier tier : mod.tiers()) {

                // Tier eligibility
                if (tier.ilvl() > itemIlvl) continue;
                if (tier.ilvl() < minModLevel) continue;

                int w = tier.weight();
                totalWeight += w;

                // Success condition:
                // same mod ID AND tier <= target tier (better or equal)
                if (mod.id().equals(target.modId())
                        && tier.tier() <= target.tier().tier()) {
                    successWeight += w;
                }
            }
        }

        if (totalWeight == 0) {
            System.out.println("ERROR: No eligible mods in pool.");
        }

        // Per-roll probability
        double p = (double) successWeight / (double) totalWeight;

        if (p <= 0) {
            System.out.println("Chance is 0%. Impossible to hit.");
        }

        // Inverse binomial thresholds
        double rolls25 = Math.log(1 - 0.25) / Math.log(1 - p);
        double rolls50 = Math.log(1 - 0.50) / Math.log(1 - p);
        double rolls75 = Math.log(1 - 0.75) / Math.log(1 - p);

        // Binomial probability for given attempts

        return new BinomialResult(p, (int)Math.ceil(rolls25), (int)Math.ceil(rolls50), (int)Math.ceil(rolls75));
    }
}
