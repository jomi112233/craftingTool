package com.jomi.GUI.simlatorParts.Nodes;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.ItemRegistry;
import com.jomi.Util.ModRoller.RolledMod;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;

public class EndNode extends NodeView {

    private LoadedItem finalItem;

    public EndNode(Node node, Project project) {
        super(node, project);
        setTitle("End Node");
    }

    @Override
    protected VBox buildContent() {
        VBox box = new VBox(10);
        box.setFillWidth(true);

        if (finalItem == null) {
            Label waiting = new Label("Run simulation to see final item.");
            waiting.getStyleClass().add("node-section-title");
            box.getChildren().add(waiting);
            return box;
        }

        // ----- PREFIXES -----
        Label prefixTitle = new Label("Prefix");
        prefixTitle.getStyleClass().add("node-section-title");

        VBox prefixList = new VBox(4);
        for (RolledMod p : finalItem.getPrefix()) {

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
        Label suffixTitle = new Label("Suffix");
        suffixTitle.getStyleClass().add("node-section-title");

        VBox suffixList = new VBox(4);
        for (RolledMod s : finalItem.getSuffix()) {

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


    private void saveFinal() {
        getProject().saveCompletedBaseItem();
        setInfo("Saved!");
    }

    @Override
    public LoadedItem execute(LoadedItem item) {
        this.finalItem = item;              // store final item
        getProject().setFinalSimulatedItem(item);
        getProject().saveCompletedBaseItem();

        refresh();                          // rebuild UI with final item
        return item;
    }
}

