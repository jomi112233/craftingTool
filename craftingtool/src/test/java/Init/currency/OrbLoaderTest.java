package Init.currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.basicCurrency.OrbFile;
import com.jomi.Handlers.Init.basicCurrency.OrbLoader;

public class OrbLoaderTest {
    @Test
    void orbFileLoader(){
        var url = getClass().getClassLoader().getResource("currency/basicOrbs.json");
        assertNotNull(url, "test resource not found");

        Path path;
        try {
            path = Path.of(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);    
        }

        OrbFile file = OrbLoader.loadFromFile(path);

        assertNotNull(file);
        assertEquals("basic", file.currencyType());
        assertEquals(17, file.orbs().size());

        var chaos = file.orbs().get(0);
        assertEquals("transmutation_orb", chaos.id());
        assertEquals("orb of transmutation", chaos.name());
        assertEquals("magic", chaos.newRarity());
        assertEquals(0, chaos.removeModifierCount());
        assertEquals(1, chaos.addedModifierCount());


    }

    @Test
    void throwsWhenFileMissing() {
        var url = getClass().getClassLoader().getResource("currency/test.json"); 
        assertNull(url, "Resource unexpectedly exists");

        Path path = Path.of("nonexistent-file.json");
        assertThrows(RuntimeException.class, () -> OrbLoader.loadFromFile(path));
    }

}
