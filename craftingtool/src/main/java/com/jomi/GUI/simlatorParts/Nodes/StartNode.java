package com.jomi.GUI.simlatorParts.Nodes;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class StartNode extends NodeView {

    private boolean expanded = false;
    private VBox contentBox;

    public StartNode(Node node, Project project) {
        super(node, project);
        setTitle("Input");
        refresh();

        setOnMouseClicked(e -> {
            if (e.getTarget() instanceof Circle) return; // don't toggle when clicking ports
            expanded = !expanded;
            refresh();
        });
    }

    @Override
    public void refresh() {
        if (contentBox != null) {
            getChildren().remove(contentBox);
        }

        contentBox = expanded ? buildExpandedUI() : buildCompactUI();
        getChildren().add(contentBox);
        contentBox.toFront();
        getTitleLabel().toFront();
        System.out.println("refresh");
    }

    private VBox buildCompactUI() {
        LoadedItem item = getProject().getBaseItem();

        VBox box = new VBox(3);
        box.setLayoutX(10);
        box.setLayoutY(30);

        if (item == null) {
            box.getChildren().add(new Label("No base item"));
            return box;
        }

        Label summary = new Label(
            "Lvl " + item.getItemLevel() +
            "\n • " + item.getItemRarity() +
            "\n • " + item.getLoadedItemClass()
        );
        summary.setStyle("-fx-text-fill: white;");

        box.getChildren().add(summary);
        return box;
    }



    private VBox buildExpandedUI() {
        LoadedItem item = getProject().getBaseItem();

        VBox box = new VBox(5);
        box.setLayoutX(10);
        box.setLayoutY(30);

        if (item == null) {
            box.getChildren().add(new Label("No base item"));
            return box;
        }

        TextField levelField = new TextField(String.valueOf(item.getItemLevel()));

        levelField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int newLevel = Integer.parseInt(newVal);

                // Update the item
                item.setItemLevel(newLevel);

                // Save immediately
                getProject().saveBaseItem();

            } catch (NumberFormatException ignored) {
                // Ignore invalid input while typing (e.g. empty, "-", letters)
            }
        });


        // RARITY FIELD
        ComboBox<String> rarityBox = new ComboBox<>();
        rarityBox.getItems().addAll("Normal", "Magic", "Rare", "Unique");
        rarityBox.setValue(item.getItemRarity());
        rarityBox.setOnAction(e -> {
            item.setItemRarity(rarityBox.getValue());
            getProject().saveBaseItem();
            refresh();
        });

        // CLASS FIELD
        ComboBox<String> classBox = new ComboBox<>();
        classBox.getItems().addAll(ModRegistry.getModCountPerItemClass().keySet());
        classBox.setValue(item.getLoadedItemClass());
        classBox.setOnAction(e -> {
            item.setLoadedItemClass(classBox.getValue());
            getProject().saveBaseItem();
            refresh();
        });


        box.getChildren().addAll(
            new Label("Item Level:"), levelField,
            new Label("Rarity:"), rarityBox,
            new Label("Class:"), classBox
        );

        return box;
    }
}

