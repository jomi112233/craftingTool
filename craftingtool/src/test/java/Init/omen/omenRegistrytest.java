package Init.omen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.omen.OmenFile;
import com.jomi.Handlers.registry.Omenregistry;

public class omenRegistrytest {
    
    @BeforeEach
    void resetRegistry() {
        Omenregistry.registerAll(new OmenFile("baseOmen", List.of(
            new Omen("greater_exaltation", "Greater Exaltation", "exalted_orb", "basic", "BOTH", false, 1),
            new Omen("homogenising_exaltation", "Homogenising Exaltation", "exalted_orb", "basic", "BOTH", false, 0)
        )));
    }

    @Test
    void registerOmensCorrectly() {
        Omen greater_exalt = Omenregistry.get("greater_exaltation");
        assertNotNull(greater_exalt);
        assertEquals("Greater Exaltation", greater_exalt.name());

        Omen homogenising_exalt = Omenregistry.get("homogenising_exaltation");
        assertNotNull(homogenising_exalt);
        assertEquals("Homogenising Exaltation", homogenising_exalt.name());
    }

    @Test
    void returnsNullForUnkwonOmen() {
        assertNull(Omenregistry.get("unknown_omen"));
    }

    @Test
    void CorrectNumeberOfOmens() {
        assertEquals(2, Omenregistry.size());
    }

    @Test
    void getAllReturnsAll() {
        Collection<Omen> all = Omenregistry.getAll();
        assertEquals(2, Omenregistry.size());
        assertTrue(all.stream().anyMatch(o -> o.id().equals("greater_exaltation")));
        assertTrue(all.stream().anyMatch(o -> o.id().equals("homogenising_exaltation")));
    }


    @Test
    void clearWorks() {
        Omenregistry.clear();
        assertEquals(0, Omenregistry.size());
        assertNull(Omenregistry.get("greater_exaltation"));
    }

    @Test
    void registerAllThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> Omenregistry.registerAll(null));
    }
}
