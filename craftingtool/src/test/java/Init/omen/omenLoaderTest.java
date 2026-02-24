package Init.omen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.basicCurrency.OrbLoader;
import com.jomi.Handlers.Init.omen.OmenFile;
import com.jomi.Handlers.Init.omen.OmenLoader;

public class omenLoaderTest {

    @Test
    void omenFileLoader() {
        var url = getClass().getClassLoader().getResource("currency/baseOmens.json");
        assertNotNull(url);

        Path path;
        try {
            path = Path.of(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OmenFile file = OmenLoader.loadFromFile(path);

        assertNotNull(file);
        assertEquals("baseOmen", file.omenType());
        assertEquals(8, file.omens().size());

        var greaterExalt = file.omens().get(0);
        assertEquals("greater_exaltation", greaterExalt.id());
        assertEquals("Greater Exaltation", greaterExalt.name());
        assertEquals("exalted_orb", greaterExalt.usedWith());
        assertEquals("basic", greaterExalt.usedWithCurrencyType());
        assertEquals("BOTH", greaterExalt.targetAffix());
        assertEquals(false, greaterExalt.targetTags());
        assertEquals(1, greaterExalt.addExtraModifiers());
    }

    @Test
    void throwsWhenFileMissing() {
        var url = getClass().getClassLoader().getResource("currency/test.json"); 
        assertNull(url, "Resource unexpectedly exists");

        Path path = Path.of("nonexistent-file.json");
        assertThrows(RuntimeException.class, () -> OrbLoader.loadFromFile(path));
    }

}
