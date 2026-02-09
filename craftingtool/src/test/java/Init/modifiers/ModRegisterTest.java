package Init.modifiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;
import com.jomi.Handlers.registry.ModRegistry;

public class ModRegisterTest {

    @Test
    void registerIndexesCorrectly() {
        List<ModFile> modFiles = ModLoader.loadAll("modifiers/");

        ModRegistry.ModRegistrySummary register = ModRegistry.registerAll(modFiles);
                            //12
        assertEquals(8, register.modCount());
        assertEquals(2, register.itemClassCount());

        assertNotNull(ModRegistry.getById("inc_ele_dmg_attacks"));
        assertEquals(6, ModRegistry.getItemByClass("weapons/warStaff").size());
        assertEquals(6, ModRegistry.getItemByClass("weapons/talisman").size());
    }
    
}
