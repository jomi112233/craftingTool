package Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.modifiers.StatRange;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller;

class ModRollerTest {

/*     @BeforeEach
    void setup() {
        ModRegistry.clear();

        Mod testMod = new Mod(
            "crit_damage_bonus",
            false,
            true,
            "+% to Critical Damage Bonus",
            List.of("Critical", "Attack", "Damage"),
            List.of( 
                new ModTier(6, 8, 1000, Map.of("crit_damage", new StatRange(10, 11))), 
                new ModTier(5, 21, 1000, Map.of("crit_damage", new StatRange(12, 13))) 
            )
        );

        ModRegistry.register("weapons/testWeapon", List.of(testMod));
    }


    @Test
    void rollReturnsMod() {
        var rolled = ModRoller.rollRandomTier("weapons/testWeapon", 50, ModRoller.ModType.SUFFIX);
        assertNotNull(rolled);
    }

    @Test
    void tagInclusion() {
        var rolled = ModRoller.rollRandomTier("weapons/testWeapon", 50, ModRoller.ModType.SUFFIX);
        assertEquals(List.of("Critical", "Attack", "Damage"), rolled.tags());
    }

    @Test
    void itemLevelFilter() {
        var rolled = ModRoller.rollRandomTier("weapons/testWeapon", 10, ModRoller.ModType.SUFFIX);
        assertEquals(6, rolled.tier().tier());
    }


    @Test
    void prefixFilter() {
        assertThrows(IllegalStateException.class, () ->
            ModRoller.rollRandomTier("weapons/testWeapon", 50, ModRoller.ModType.PREFIX));
    }  

    @Test
    void testWeightedSelectionDoesNotCrash() {
        for (int i = 0; i < 1000; i++) {
            var rolled = ModRoller.rollRandomTier("weapons/testWeapon", 50, ModRoller.ModType.SUFFIX);
            assertNotNull(rolled);
        }
    } */
}