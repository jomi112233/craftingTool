package Init.item;

import org.junit.jupiter.api.*;

import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ItemRegistry;

import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ItemRegistryTest {

    private Path tempDir;

    @BeforeEach
    void setup() throws IOException {
        tempDir = Files.createTempDirectory("itemRegistryTest");
    }

    @AfterEach
    void cleanup() throws IOException {
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a)) // delete children first
                .forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (IOException ignored) {}
                });
        }
    }

    @Test
    void saveAndLoadJsonWorks() {
        LoadedItem item = new LoadedItem();
        item.setItemLevel(10);
        item.setItemRarity("rare");
        item.setLoadedItemClass("weapon");

        Path file = tempDir.resolve("item.json");

        ItemRegistry.saveToJson(item, file);
        assertTrue(Files.exists(file));

        LoadedItem loaded = ItemRegistry.loadFromJson(file);

        assertEquals(10, loaded.getItemLevel());
        assertEquals("rare", loaded.getItemRarity());
        assertEquals("weapon", loaded.getLoadedItemClass());
    }

    @Test
    void loadFromJsonThrowsWhenFileMissing() {
        Path missing = tempDir.resolve("does_not_exist.json");

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ItemRegistry.loadFromJson(missing));

        assertTrue(ex.getMessage().contains("Failed to load item"));
    }

    @Test
    void saveToJsonThrowsWhenPathInvalid() {
        LoadedItem item = new LoadedItem();
        Path invalid = tempDir.resolve("folder/that/does/not/exist/item.json");

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ItemRegistry.saveToJson(item, invalid));

        assertTrue(ex.getMessage().contains("Failed to save item"));
    }

    @Test
    void createNewItemCreatesFilesAndDirectories() {
        Path project = tempDir.resolve("project");

        LoadedItem item = ItemRegistry.createNewItem(project, 50, "magic", "armor");

        assertEquals(50, item.getItemLevel());
        assertEquals("magic", item.getItemRarity());
        assertEquals("armor", item.getLoadedItemClass());

        assertTrue(Files.exists(project));
        assertTrue(Files.exists(project.resolve("itemruns")));

        assertTrue(Files.exists(project.resolve("baseitem.json")));
        assertTrue(Files.exists(project.resolve("baseitemCompleted.json")));

        LoadedItem loaded = ItemRegistry.loadFromJson(project.resolve("baseitem.json"));
        assertEquals(50, loaded.getItemLevel());
    }

    @Test
    void createNewItemThrowsWhenDirectoryInvalid() {
        Path invalid = tempDir.resolve("notADir");
        try {
            Files.writeString(invalid, "I am a file");
        } catch (IOException ignored) {}

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ItemRegistry.createNewItem(invalid, 1, "normal", "weapon"));

        assertTrue(ex.getMessage().contains("Failed to create new item"));
    }
}

