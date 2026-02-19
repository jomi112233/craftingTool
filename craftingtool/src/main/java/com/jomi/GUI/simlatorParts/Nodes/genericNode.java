package com.jomi.GUI.simlatorParts.Nodes;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GenericNode extends NodeView {

    public GenericNode(Node node, Project project) {
        super(node, project);
    }

    @Override
    protected VBox buildContent() {
        VBox box = new VBox();
        box.getChildren().add(new Label("No content"));
        return box;
    }
}

