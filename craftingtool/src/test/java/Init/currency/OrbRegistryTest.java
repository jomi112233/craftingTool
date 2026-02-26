package Init.currency;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.registry.OrbRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrbRegistryTest {

    /* @BeforeEach
    void resetRegistry() {
        OrbRegistry.registerAll(new OrbFile("basic", List.of(
            new Orb("chaos_orb", "Chaos Orb", "normal", "rare", 0, 6, "item", "any"),
            new Orb("transmute_orb", "Orb of Transmutation", "normal", "magic", 0, 2, "item", "any")
        )));
    }

    @Test
    void registersOrbsCorrectly() {
        Orb chaos = OrbRegistry.get("chaos_orb");
        assertNotNull(chaos);
        assertEquals("Chaos Orb", chaos.name());

        Orb transmute = OrbRegistry.get("transmute_orb");
        assertNotNull(transmute);
        assertEquals("Orb of Transmutation", transmute.name());
    }

    @Test
    void returnsNullForUnknownOrb() {
        assertNull(OrbRegistry.get("unknown_orb"));
    }

    @Test
    void correctNumberOfOrbs() {
        assertEquals(2, OrbRegistry.size());
    }

    @Test
    void getAllReturnsAll() {
        Collection<Orb> all = OrbRegistry.getAll();
        assertEquals(2, OrbRegistry.size());
        assertTrue(all.stream().anyMatch(o -> o.id().equals("chaos_orb")));
        assertTrue(all.stream().anyMatch(o -> o.id().equals("transmute_orb")));
    }

    @Test
    void clearWorks() {
        OrbRegistry.clear();
        assertEquals(0, OrbRegistry.size());
        assertNull(OrbRegistry.get("chaos_orb"));
    }

    @Test
    void registerAllThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> OrbRegistry.registerAll(null));
    } */
}
