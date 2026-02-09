package currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;

public class validBasicCurrencyTest {
    OrbFile file = OrbLoader.loadFromResources("validTest/currency/basicOrbs.json");

    @Test
    void loadAllbasicCurrency(){
        assertNotNull(file);
        assertEquals("basic", file.currencyType());
        assertNotNull(file.orbs());
        assertFalse(file.orbs().isEmpty());
        assertEquals(6, file.orbs().size());
    }

    @Test
    void orbFieldtest(){
        Orb exalt = file.orbs().stream().filter(o -> o.id().equals("exalted_orb")).findFirst().orElse(null);

        assertNotNull(exalt);
        assertEquals("exalted orb", exalt.name());
        assertEquals("rare", exalt.minRarity()); 
        assertEquals("rare", exalt.newRarity()); 
        assertEquals(0, exalt.removeModifierCount()); 
        assertEquals(1, exalt.addedModifierCount()); 
        assertEquals("any", exalt.target());
    }


    @Test
    void testAllOrbsHaveValidFields() {
        for (Orb orb : file.orbs()) {
            assertNotNull(orb.id());
            assertNotNull(orb.name());
            assertNotNull(orb.minRarity());
            assertNotNull(orb.newRarity());
            assertNotNull(orb.target());

            assertTrue(orb.removeModifierCount() >= 0);
            assertTrue(orb.addedModifierCount() >= 0);
        }
    }

    @Test
    void MissingFileError(){
        assertThrows(RuntimeException.class, 
            () -> {OrbLoader.loadFromResources("validTest/currency/noReal.json");
        });
    }
}
