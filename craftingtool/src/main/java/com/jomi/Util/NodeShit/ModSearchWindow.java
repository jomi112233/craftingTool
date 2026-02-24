package com.jomi.Util.NodeShit;

import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.TurriHaku;
import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.Init.modifiers.ModTier;
import com.jomi.Handlers.Item.LoadedItem;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ModSearchWindow {

    // Listener now returns BOTH mod + tier
    public interface ModSelectListener {
        void onModSelected(Mod mod, ModTier tier);
    }

    // Wrapper entry for the ListView (does NOT modify Mod)
    private record ModTierEntry(Mod mod, ModTier tier) {
        @Override
        public String toString() {
            return "T" + tier.tier() + " " + mod.name();
        }
    }

    public static void open(LoadedItem item, ModSelectListener listener) {
        Stage stage = new Stage();
        stage.setTitle("Search Modifiers");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField searchField = new TextField();
        searchField.setPromptText("Search modifiers...");

        ListView<ModTierEntry> listView = new ListView<>();

        // Load only mods valid for this item class
        List<Mod> allMods = ModRegistry.getItemByClass(item.getLoadedItemClass());

        // Expand each mod into multiple entries (one per tier)
        List<ModTierEntry> allEntries = allMods.stream()
            .flatMap(mod -> mod.tiers().stream()
                .map(tier -> new ModTierEntry(mod, tier)))
            .toList();

        listView.getItems().setAll(allEntries);

        // Live search
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV.toLowerCase();

            List<ModTierEntry> filtered = allEntries.stream()
                .filter(e ->
                    e.mod().name().toLowerCase().contains(q) ||
                    e.mod().id().toLowerCase().contains(q))
                .toList();

            listView.getItems().setAll(filtered);
        });

        // Click to select
        listView.setOnMouseClicked(e -> {
            ModTierEntry selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listener.onModSelected(selected.mod(), selected.tier());
                stage.close();
            }
        });

        root.getChildren().addAll(searchField, listView);

        stage.setScene(new Scene(root, 450, 600));
        stage.show();
    }
}
