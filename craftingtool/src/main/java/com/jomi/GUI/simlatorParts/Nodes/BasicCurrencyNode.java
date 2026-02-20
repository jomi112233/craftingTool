package com.jomi.GUI.simlatorParts.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.basicCurrency.Orb;
import com.jomi.Handlers.Init.omen.Omen;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Handlers.registry.Omenregistry;
import com.jomi.Handlers.registry.OrbRegistry;
import com.jomi.Util.craftingAction.BasicCurrencyAction;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BasicCurrencyNode extends NodeView {

    public BasicCurrencyNode(Node node, Project project) {
        super(node, project);
        setTitle("Basic Currency");
        refresh();
    }

    @Override
    protected VBox buildContent() {
        VBox box = new VBox(8);

        Node data = getNode();

        // Show selected actions
        if (!data.getActions().isEmpty()) {
            VBox selectedBox = new VBox(4);
            Label selectedLabel = new Label("Selected:");
            selectedLabel.getStyleClass().add("node-section-title");
            selectedBox.getChildren().add(selectedLabel);

            // First action = currency
            String currency = data.getActions().get(0);
            Label currencyLabel = new Label("• " + currency);
            currencyLabel.getStyleClass().add("node-selected-currency");
            selectedBox.getChildren().add(currencyLabel);

            // Remaining actions = omens
            for (int i = 1; i < data.getActions().size(); i++) {
                String omen = data.getActions().get(i);
                Label omenLabel = new Label("• " + omen);
                omenLabel.getStyleClass().add("node-selected-omen");
                selectedBox.getChildren().add(omenLabel);
            }

            box.getChildren().add(selectedBox);
        }

        // Show dropdown depending on state
        if (data.getActions().isEmpty()) {
            box.getChildren().add(buildCurrencyDropdown());
        } else {
            box.getChildren().add(buildOmenDropdown());
        }

        return box;
    }



    private VBox buildCurrencyDropdown() {
        VBox box = new VBox(6);

        Label label = new Label("Select Currency:");
        ComboBox<String> combo = new ComboBox<>();

        OrbRegistry.getAll().forEach(orb -> combo.getItems().add(orb.id()));

        combo.setOnAction(e -> {
            String selected = combo.getValue();
            if (selected != null) {
                getNode().setAction(selected);  // <-- FIXED
                refresh();
            }
        });

        box.getChildren().addAll(label, combo);
        return box;
    }



    private VBox buildOmenDropdown() {
        VBox box = new VBox(6);

        Label label = new Label("Add Omen:");
        ComboBox<String> combo = new ComboBox<>();

        Omenregistry.getAll().forEach(omen -> combo.getItems().add(omen.id()));

        combo.setOnAction(e -> {
            String selected = combo.getValue();
            if (selected != null) {
                getNode().setAction(selected);
                refresh();
            }
        });

        box.getChildren().addAll(label, combo);
        return box;
    }

    @Override
    public LoadedItem execute(LoadedItem item) {

        List<String> actions = getNode().getActions();

        if (actions.isEmpty()) {
            return item; // nothing to do
        }

        // First action = currency
        String orbId = actions.get(0);
        Orb orb = OrbRegistry.get(orbId);

        // Remaining actions = omens
        List<Omen> omens = new ArrayList<>();
        for (int i = 1; i < actions.size(); i++) {
            Omen o = Omenregistry.get(actions.get(i));
            if (o != null) omens.add(o);
        }

        BasicCurrencyAction.apply(item, orb, omens);

        return item;
    }




    
}

