package Util.CurrencyAction;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.modifiers.StatRange;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Util.CurrencyAction;
import com.jomi.Util.ModRoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyActionTest {

    @BeforeEach
    void setup() {
        ModRegistry.clear();

        Mod fakeModPrefix = new Mod(
            "test_mod",
            true,
            false,
            "Test Mod",
            List.of("TestTag"),
            List.of(
                new ModTier(1, 1, 1000, Map.of("test_stat", new StatRange(1, 2)))
            )
        );

        Mod fakeModSuffix = new Mod(
            "test_mod",
            false,
            true,
            "Test Mod",
            List.of("TestTag"),
            List.of(
                new ModTier(1, 1, 1000, Map.of("test_stat", new StatRange(1, 2)))
            )
        );

        ModRegistry.register("weapons/warStaff", List.of(fakeModSuffix));
        ModRegistry.register("weapons/warStaff", List.of(fakeModPrefix));

        OrbRegistry.clear();
    }




    @Test
    void testTransmuteUpgradesNormalToMagic() {
        Orb transmute = OrbLoaderTest.orb(
            "transmute",
            "Orb of Transmutation",
            "normal",
            "magic",
            0,
            1,
            "BOTH"
        );

        OrbRegistry.registerAll(new OrbFile("basic", List.of(transmute)));

        var item = ItemLoaderTest.emptyItem("normal");

        var updated = CurrencyAction.apply(item, "transmute");

        assertEquals("magic", updated.itemRarity());
        assertEquals(1, updated.prefix().size() + updated.suffix().size());
    }

    @Test
    void testExaltedAddsOneModToRare() {
        Orb exalt = OrbLoaderTest.orb(
            "exalted",
            "Exalted Orb",
            "rare",
            "any",
            0,
            1,
            "BOTH"
        );

        OrbRegistry.registerAll(new OrbFile("basic", List.of(exalt)));

        var item = ItemLoaderTest.emptyItem("rare");

        var updated = CurrencyAction.apply(item, "exalted");

        assertEquals("rare", updated.itemRarity());
        assertEquals(1, updated.prefix().size() + updated.suffix().size());
    }

    @Test
    void testExaltedDoesNothingWhenFull() {
        Orb exalt = OrbLoaderTest.orb(
            "exalted",
            "Exalted Orb",
            "rare",
            "any",
            0,
            1,
            "BOTH"
        );

        OrbRegistry.registerAll(new OrbFile("basic", List.of(exalt)));

        var fullItem = ItemLoaderTest.withMods(
            "rare",
            List.of(
                dummyMod(), dummyMod(), dummyMod()
            ),
            List.of(
                dummyMod(), dummyMod(), dummyMod()
            )
        );

        var updated = CurrencyAction.apply(fullItem, "exalted");

        assertEquals(3, updated.prefix().size());
        assertEquals(3, updated.suffix().size());
    }

    @Test
    void testChaosRemovesAndAdds() {
        Orb chaos = OrbLoaderTest.orb(
            "chaos",
            "Chaos Orb",
            "magic",
            "rare",
            1,
            1,
            "BOTH"
        );

        OrbRegistry.registerAll(new OrbFile("basic", List.of(chaos)));

        var item = ItemLoaderTest.withMods(
            "magic",
            List.of(dummyMod()),
            List.of(dummyMod())
        );

        var updated = CurrencyAction.apply(item, "chaos");

        assertEquals("rare", updated.itemRarity());
        assertEquals(2, updated.prefix().size() + updated.suffix().size());
    }

    @Test
    void testPrefixTargetOnlyAddsPrefix() {
        Orb prefixOrb = OrbLoaderTest.orb(
            "prefix_add",
            "Prefix Orb",
            "magic",
            "any",
            0,
            1,
            "PREFIX"
        );

        OrbRegistry.registerAll(new OrbFile("basic", List.of(prefixOrb)));

        var item = ItemLoaderTest.emptyItem("magic");

        var updated = CurrencyAction.apply(item, "prefix_add");

        assertEquals(1, updated.prefix().size());
        assertEquals(0, updated.suffix().size());
    }

    private ModRoller.RolledMod dummyMod() {
        return new ModRoller.RolledMod(
            "dummy",
            "Dummy Mod",
            List.of("Test"),
            new ModTier(1, 1, 1, Map.of())
        );
    }
}
