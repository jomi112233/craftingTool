package com.jomi.Util;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import com.jomi.App;
import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ItemRegistry;
import com.jomi.Handlers.registry.ModRegistry;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class ProjectActions {

    private Stage mainStage;

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public Stage getMainStage() {
        return mainStage;
    }



    public record ProjectCreationData(String name, String itemType, int itemLevel) {}

    public static void createNewProject() {

        // Record to carry dialog results
        record ProjectCreationData(String name, String itemType, int itemLevel, String itemRarity) {}

        Dialog<ProjectCreationData> dialog = new Dialog<>();
        dialog.setTitle("Create Project");
        dialog.setHeaderText("Enter project details");

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Fields
        TextField nameField = new TextField();
        nameField.setPromptText("Project name");

        ComboBox<String> itemTypeSelector = new ComboBox<>();
        itemTypeSelector.getItems().addAll(ModRegistry.getModCountPerItemClass().keySet());
        itemTypeSelector.setPromptText("Select item type");

        TextField itemLevelField = new TextField();
        itemLevelField.setPromptText("Item level (1–100)");

        ComboBox<String> raritySelector = new ComboBox<>();
        raritySelector.getItems().addAll("Normal", "Magic", "Rare", "Unique");
        raritySelector.setPromptText("Select rarity");

        VBox content = new VBox(
            10,
            new Label("Project Name:"), nameField,
            new Label("Item Type:"), itemTypeSelector,
            new Label("Item Level:"), itemLevelField,
            new Label("Item Rarity:"), raritySelector
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == createButtonType) {
                int level = Integer.parseInt(itemLevelField.getText());
                return new ProjectCreationData(
                    nameField.getText(),
                    itemTypeSelector.getValue(),
                    level,
                    raritySelector.getValue()
                );
            }
            return null;
        });

        Optional<ProjectCreationData> result = dialog.showAndWait();

        result.ifPresent(data -> {
            try {
                var pm = App.getInstance().getProjectManager();
                pm.newProject(data.name());

                Project project = pm.getCurrentProject();
                project.setItemType(data.itemType());

                // Create item files with user selections
                Path projectFolder = project.getProjectFolder();
                LoadedItem item = ItemRegistry.createNewItem(
                    projectFolder,
                    data.itemLevel(),
                    data.itemRarity(),
                    data.itemType() // used as loadedItemClass
                );

                project.setBaseItem(item);


                // Apply user selections
                item.setItemLevel(data.itemLevel());
                item.setItemRarity(data.itemRarity());
                item.setLoadedItemClass(data.itemType());

                // Save updated item back to disk (IMPORTANT!)
                ItemRegistry.saveToJson(item, projectFolder.resolve("baseitem.json"));
                ItemRegistry.saveToJson(item, projectFolder.resolve("baseitemCompleted.json"));

                project.setBaseItem(item);

                App.getInstance().getGui().showSimulator();
                System.out.println("New project created successfully.");

            } catch (Exception e) {
                System.err.println("Failed to create new project: " + e.getMessage());
            }
        });
    }


    public static void loadProject() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Project");

        chooser.setInitialDirectory(
            ConfigLoader.getDataFolder()
                .resolve("saveState")
                .resolve("SceneData")
                .toFile()
        );

        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Project Files (*.json)", "*.json")
        );

        // We need a window reference — safest way:
        Window window = App.getInstance().getGui().getStage();

        File file = chooser.showOpenDialog(window);

        if (file != null) {
            String name = file.getName().replace(".json", "");

            Project loaded = App.getInstance()
                                .getProjectManager()
                                .loadProject(name);

            if (loaded != null) {
                App.getInstance().getGui().showSimulator();
                System.out.println("Loaded project: " + name);
            } else {
                System.err.println("Failed to load project: " + name);
            }
        }
    }


    public static void saveProject() {
        System.out.println("Saving");
    }
}


