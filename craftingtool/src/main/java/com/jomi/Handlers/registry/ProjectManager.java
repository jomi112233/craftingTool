package com.jomi.Handlers.registry;

import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;

public class ProjectManager {

    private Project currentProject;

    public void newProject(String name) {
        this.currentProject = new Project(name);
        saveProject(currentProject);
    }

    public void setProject(Project project) {
        currentProject = project;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public boolean hasproject() {
        return currentProject != null;
    }

    public void saveProject(Project project) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Path base = ConfigLoader.getDataFolder();
            Path saveFolder = base.resolve("saveState").resolve("SceneData");
            Files.createDirectories(saveFolder);

            Path file = saveFolder.resolve(project.getName() + ".json");

            mapper.writeValue(file.toFile(), project);

            System.out.println("Project saved to " + file.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Failed to save project: " + e.getMessage());
        }
    }

    public Project loadProject(String name) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            Path base = ConfigLoader.getDataFolder();
            Path saveFolder = base.resolve("saveState").resolve("SceneData");
            Path file = saveFolder.resolve(name + ".json");

            Project loaded = mapper.readValue(file.toFile(), Project.class);

            Path itemPath = loaded.getProjectFolder().resolve("baseitem.json");
            LoadedItem item = ItemRegistry.loadFromJson(itemPath);
            loaded.setBaseItem(item);

            this.currentProject = loaded;

            System.out.println("Loaded project: " + loaded.getName());
            return loaded;

        } catch (Exception e) {
            System.err.println("Failed to load project: " + e.getMessage());
            return null;
        }
    }

    public void saveFinalItem(Project project, LoadedItem item, int index) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Folder where the project stores its item data
            Path folder = project.getProjectFolder();
            Files.createDirectories(folder);

            // File name: result_1.json, result_2.json, ...
            Path file = folder.resolve("result_" + index + ".json");

            mapper.writeValue(file.toFile(), item);

            System.out.println("Saved branch result to: " + file.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Failed to save final item: " + e.getMessage());
        }
    }


}
