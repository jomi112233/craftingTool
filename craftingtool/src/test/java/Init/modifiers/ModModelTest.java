package Init.modifiers;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Init.modifiers.StatRange;

import static org.junit.jupiter.api.Assertions.*;

class ModModelTest {

    @Test
    void testStatRangeValues() {
        StatRange range = new StatRange();
        range.min = 5;
        range.max = 10;

        assertTrue(range.min < range.max);
    }

    @Test
    void testModTierStructure() {
        ModTier tier = new ModTier();
        tier.tier = 1;
        tier.ilvl = 10;
        tier.weight = 100;

        assertEquals(1, tier.tier);
        assertEquals(10, tier.ilvl);
    }
}
