package currency;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.registry.OrbRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrbRegistryTest {

    @BeforeEach
    void resetRegistry() {
        // Re-register with fresh data each test
        OrbRegistry.registerAll(new OrbFile("basic", List.of(
            new Orb("chaos_orb", "Chaos Orb", "normal", "rare", 0, 6, "item"),
            new Orb("transmute_orb", "Orb of Transmutation", "normal", "magic", 0, 2, "item")
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
}
