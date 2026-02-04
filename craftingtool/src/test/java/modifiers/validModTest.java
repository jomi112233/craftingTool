package modifiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.Init.modifiers.StatRange;

public class validModTest {
    List<Mod> mods = ModLoader.loadDirectory("src/test/resources/validTest");

    
    @Test
    void loadAllModifiers(){
        assertFalse(mods.isEmpty());
        assertEquals(12, mods.size());
    }

    @Test
    void testPrefixAndsuffixFiles(){    
        boolean hasPrefix = mods.stream().anyMatch(m -> m.prefix);
        boolean hasSuffix = mods.stream().anyMatch(m -> m.suffix);

        assertTrue(hasPrefix);
        assertTrue(hasSuffix);
    }

    @Test
    void testItemAssigment(){
        Mod warrStaffMod = mods.stream().filter(m -> m.itemClass.equals("weapons/warStaff")).findFirst().orElse(null);

        assertNotNull(warrStaffMod);
    }

    @Test
    void testStatparsing(){
        Mod mod = mods.stream().filter(m -> m.id.equals("inc_ele_dmg_attacks")).findFirst().orElseThrow();

        StatRange range = mod.tiers.get(0).stats.get("ele_inc");

        assertEquals(34, range.min);
        assertEquals(47, range.max);
    }
}
