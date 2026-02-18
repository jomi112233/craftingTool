package project;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ProjectManager;

public class ProjectManagertest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        // Redirect all project saves/loads into the temp directory
        ConfigLoader.setDataFolder(tempDir);
    }

    @Test
    void testNewProjectCreatesAndSaves() throws Exception {
        ProjectManager pm = new ProjectManager();

        pm.newProject("TestProject");

        Project project = pm.getCurrentProject();
        assertNotNull(project);
        assertEquals("TestProject", project.getName());

        Path file = tempDir
            .resolve("saveState")
            .resolve("SceneData")
            .resolve("TestProject.json");

        assertTrue(Files.exists(file));
    }

    @Test
    void testSaveProjectWritesJson() throws Exception {
        ProjectManager pm = new ProjectManager();
        pm.newProject("TestProject");

        Project project = pm.getCurrentProject();
        pm.saveProject(project);

        Path file = tempDir
            .resolve("saveState")
            .resolve("SceneData")
            .resolve("TestProject.json");

        assertTrue(Files.exists(file));

        String json = Files.readString(file);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"TestProject\""));
    }


    @Test
    void testLoadProjectFailsGracefully() {
        ProjectManager pm = new ProjectManager();

        Project loaded = pm.loadProject("DoesNotExist");

        assertNull(loaded);
    }

    @Test
    void testSaveLoadRoundTrip() throws Exception {
        ProjectManager pm = new ProjectManager();

        // Create project
        pm.newProject("RoundTrip");
        Project original = pm.getCurrentProject();

        Path itemFolder = tempDir
            .resolve("saveState")
            .resolve("itemData")
            .resolve(original.getId());

        Files.createDirectories(itemFolder);

        String itemJson = """
            {
                "itemLevel": 61,
                "itemRarity": "Rare",
                "loadedItemClass": "weapons/talisman"
            }
            """;

        Files.writeString(itemFolder.resolve("baseitem.json"), itemJson);

        // Save project JSON
        pm.saveProject(original);

        // Load project
        Project loaded = pm.loadProject("RoundTrip");

        assertNotNull(loaded);
        assertEquals(original.getName(), loaded.getName());
        assertEquals(original.getItemType(), loaded.getItemType());
    }

}
