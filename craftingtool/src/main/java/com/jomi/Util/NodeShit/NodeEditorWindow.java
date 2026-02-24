package com.jomi.Util.NodeShit;

import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller;
import com.jomi.Util.ModRoller.RolledMod;
import com.jomi.Handlers.Init.modifiers.Mod;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NodeEditorWindow {

    public static void open(LoadedItem item, Runnable onClose, Stage previousStage) {

        if (previousStage != null) {
            previousStage.close();
        }

        Stage stage = new Stage();
        stage.setTitle("Edit Base Item");

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));

        // ---------------- ITEM LEVEL ----------------
        Label ilvlLabel = new Label("Item Level:");
        TextField ilvlField = new TextField(String.valueOf(item.getItemLevel()));

        ilvlField.textProperty().addListener((obs, oldV, newV) -> {
            try {
                int lvl = Integer.parseInt(newV);
                item.setItemLevel(lvl);
                onClose.run();
            } catch (NumberFormatException ignored) {}
        });

        // ---------------- ITEM RARITY ----------------
        Label rarityLabel = new Label("Rarity:");
        ComboBox<String> rarityBox = new ComboBox<>();
        rarityBox.getItems().addAll("Normal", "Magic", "Rare", "Unique");
        rarityBox.setValue(item.getItemRarity());

        rarityBox.valueProperty().addListener((obs, oldV, newV) -> {
            item.setItemRarity(newV);
            onClose.run();
        });

        // ---------------- ITEM CLASS ----------------
        Label classLabel = new Label("Item Class:");
        ComboBox<String> classBox = new ComboBox<>();

        // Load all item classes from registry
        classBox.getItems().addAll(ModRegistry.getAllItemClasses());

        // Set current value
        classBox.setValue(item.getLoadedItemClass());

        // Update item when changed
        classBox.valueProperty().addListener((obs, oldV, newV) -> {
            item.setLoadedItemClass(newV);
            onClose.run(); // save immediately
        });


        // ---------------- PREFIX LIST ----------------
        Label prefixLabel = new Label("Prefixes");
        VBox prefixBox = new VBox(6);
        refreshList(prefixBox, item, true);

        // ---------------- SUFFIX LIST ----------------
        Label suffixLabel = new Label("Suffixes");
        VBox suffixBox = new VBox(6);
        refreshList(suffixBox, item, false);

        // ---------------- ADD MOD BUTTON ----------------
        Button addModBtn = new Button("Add Modifier");
        addModBtn.setOnAction(e -> {
            ModSearchWindow.open(item, (mod, tier) -> {

                RolledMod rolled = new RolledMod(mod.id(), mod.name(), mod.tags(), tier);

                if (mod.prefix()) {
                    item.getPrefix().add(rolled);
                    refreshList(prefixBox, item, true);
                    onClose.run();

                }

                if (mod.suffix()) {
                    item.getSuffix().add(rolled);
                    refreshList(suffixBox, item, false);
                    onClose.run();

                }

            });
        });

        root.getChildren().addAll(
            ilvlLabel, ilvlField,
            rarityLabel, rarityBox,
            classLabel, classBox,
            prefixLabel, prefixBox,
            suffixLabel, suffixBox,
            addModBtn
        );

        stage.setScene(new Scene(root, 500, 700));

        stage.setOnCloseRequest(e -> onClose.run());
        stage.show();
    }

    private static void refreshList(VBox box, LoadedItem item, boolean prefix) {
        box.getChildren().clear();

        var list = prefix ? item.getPrefix() : item.getSuffix();

        for (RolledMod mod : list) {

            Label label = new Label("• (T" + mod.tier().tier() + ") " + mod.name());
            label.setWrapText(true);

            Button removeBtn = new Button("✖");
            removeBtn.setOnAction(e -> {
                list.remove(mod);
                refreshList(box, item, prefix);
            });

            HBox row = new HBox(8, label, removeBtn);
            box.getChildren().add(row);
        }
    }
}
