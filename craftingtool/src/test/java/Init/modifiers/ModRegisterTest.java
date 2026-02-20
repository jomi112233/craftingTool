package Init.modifiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegistry;

public class ModRegisterTest {

    @Test
    void registerIndexesCorrectly() {
        Path testPath = Path.of("src/test/resources/modifiers");
        List<ModFile> modFiles = ModLoader.loadAll(testPath);

        ModRegistry.ModRegistrySummary summary = ModRegistry.registerAll(modFiles);

        assertEquals(8, summary.modCount());
        assertEquals(2, summary.itemClassCount());


        assertTrue(ModRegistry.getItemByClass("weapons/warStaff") != null);
        assertTrue(ModRegistry.getItemByClass("weapons/talisman") != null);


        assertEquals(6, ModRegistry.getItemByClass("weapons/warStaff").size());
        assertEquals(6, ModRegistry.getItemByClass("weapons/talisman").size());
        

        assertNotNull(ModRegistry.getById("inc_ele_dmg_attacks"));
        assertNotNull(ModRegistry.getById("inc_phys_dmg"));
    }


}
