package Init.modifiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.modifiers.ModFile;
import com.jomi.Handlers.Init.modifiers.ModLoader;

public class ModLoadertest {
    
    @Test
    void LoadJsonFiles() {

        Path testPath = Path.of("src/test/resources/modifiers");
        List<ModFile> modFiles = ModLoader.loadAll(testPath);

        assertFalse(modFiles.isEmpty());
        assertEquals("weapons/talisman", modFiles.get(0).itemClass());
    }
}
