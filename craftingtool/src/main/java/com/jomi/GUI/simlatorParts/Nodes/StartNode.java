package com.jomi.GUI.simlatorParts.Nodes;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller.RolledMod;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class StartNode extends NodeView {

    LoadedItem item = getProject().getBaseItem();

    public StartNode(Node node, Project project) {
        super(node, project);
        setTitle(extractClassName(item.getLoadedItemClass()));
        setInfo(item.getItemLevel() + "");
        applyRarityColor(item.getItemRarity());
    }

    @Override
    protected VBox buildContent() {
        VBox box = new VBox(10);
        box.setFillWidth(true);

        // ----- PREFIXES -----
        Label prefixTitle = new Label("Prefixes");
        prefixTitle.getStyleClass().add("node-section-title");

        VBox prefixList = new VBox(4);
        for (RolledMod p : item.getPrefix()) {

            String[] parts = p.name().split(",", 2);

            String text;
            if (parts.length == 2) {
                text = "• (T" + p.tier().tier() + ") "
                    + parts[0].trim() + ",\n    "
                    + parts[1].trim();
            } else {
                text = "• (T" + p.tier().tier() + ") " + p.name();
            }

            Label label = new Label(text);
            label.setWrapText(true);
            label.getStyleClass().add("node-modifier");
            prefixList.getChildren().add(label);
        }

        // ----- SUFFIXES -----
        Label suffixTitle = new Label("Suffixes");
        suffixTitle.getStyleClass().add("node-section-title");

        VBox suffixList = new VBox(4);
        for (RolledMod s : item.getSuffix()) {

            String[] parts = s.name().split(",", 2);

            String text;
            if (parts.length == 2) {
                text = "• (T" + s.tier().tier() + ") "
                    + parts[0].trim() + ",\n    "
                    + parts[1].trim();
            } else {
                text = "• (T" + s.tier().tier() + ") " + s.name();
            }

            Label label = new Label(text);
            label.setWrapText(true);
            label.getStyleClass().add("node-modifier");
            suffixList.getChildren().add(label);
        }

        box.getChildren().addAll(prefixTitle, prefixList, suffixTitle, suffixList);
        return box;
    }




    private String extractClassName(String full) {
        if (full == null) return "";
        int index = full.lastIndexOf('/');
        return index >= 0 ? full.substring(index + 1) : full;
    }

}

