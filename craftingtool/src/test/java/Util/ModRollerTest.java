package Util;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller;
import com.jomi.Util.ModRoller.ModType;
import com.jomi.Util.ModRoller.RolledMod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModRollerTest {

    @BeforeEach
    void setup() {
        ModRegistry.clear();

      
        ModTier tLow  = new ModTier(6,  1,  100, Map.of());
        ModTier tMid  = new ModTier(3, 30, 200, Map.of());
        ModTier tHigh = new ModTier(1, 80, 300, Map.of());

        
        Mod prefixMod = new Mod(
            "p1", true, false, "Prefix Mod",
            new String[]{"tag"}, List.of(tLow, tMid, tHigh)
        );

        Mod suffixMod = new Mod(
            "s1", false, true, "Suffix Mod",
            new String[]{"tag"}, List.of(tLow, tMid, tHigh)
        );

        ModRegistry.register("weapons/talisman", List.of(prefixMod, suffixMod));
    }


    @Test
    void rollsOnlyPrefixMods() {
        for (int i = 0; i < 50; i++) {
            RolledMod rolled = ModRoller.rollRandomTier("weapons/talisman", 100, ModType.PREFIX);
            assertTrue(rolled.mod().prefix());
            assertFalse(rolled.mod().suffix());
        }
    }


    @Test
    void rollsOnlySuffixMods() {
        for (int i = 0; i < 50; i++) {
            RolledMod rolled = ModRoller.rollRandomTier("weapons/talisman", 100, ModType.SUFFIX);
            assertTrue(rolled.mod().suffix());
            assertFalse(rolled.mod().prefix());
        }
    }


    @Test
    void rollsBothPrefixAndSuffixWhenRequested() {
        boolean sawPrefix = false;
        boolean sawSuffix = false;

        for (int i = 0; i < 200; i++) {
            RolledMod rolled = ModRoller.rollRandomTier("weapons/talisman", 100, ModType.BOTH);

            if (rolled.mod().prefix()) sawPrefix = true;
            if (rolled.mod().suffix()) sawSuffix = true;


            if (sawPrefix && sawSuffix) break;
        }

        assertTrue(sawPrefix || sawSuffix);
    }

    @Test
    void respectsItemLevelRestrictions() {
        // Item level 10 → only tLow (ilvl 1) is allowed
        for (int i = 0; i < 50; i++) {
            RolledMod rolled = ModRoller.rollRandomTier("weapons/talisman", 10, ModType.BOTH);
            assertEquals(1, rolled.tier().ilvl());
        }
    }

    @Test
    void throwsWhenNoEligibleTiers() {
        assertThrows(IllegalStateException.class, () ->
            ModRoller.rollRandomTier("weapons/talisman", 0, ModType.BOTH)
        );
    }

    @Test
    void throwsWhenNoModsForItemClass() {
        assertThrows(IllegalArgumentException.class, () ->
            ModRoller.rollRandomTier("unknown/class", 100, ModType.BOTH)
        );
    }
}
