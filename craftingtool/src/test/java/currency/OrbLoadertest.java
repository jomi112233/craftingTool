package currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;

public class OrbLoaderTest {
    
    @Test
    void orbFileLoader(){
        Path path = Path.of("src/test/resources/currency/basicOrbs.json");

        OrbFile file = OrbLoader.loadFromFile(path);

        assertNotNull(file);
        assertEquals("basic", file.currencyType());
        assertEquals(6, file.orbs().size());

        var chaos = file.orbs().get(4);
        assertEquals("chaos_orb", chaos.id());
        assertEquals("chaos orb", chaos.name());
        assertEquals("rare", chaos.minRarity());
        assertEquals("rare", chaos.newRarity());
        assertEquals(1, chaos.removeModifierCount());
        assertEquals(1, chaos.addedModifierCount());
        assertEquals("any", chaos.target());


    }

    @Test
    void throwsWhenFileMissing() {
        Path path = Path.of("src/test/resources/orbs/does_not_exist.json");

        assertThrows(RuntimeException.class, () -> OrbLoader.loadFromFile(path));
    }

}
