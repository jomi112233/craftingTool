package com.jomi.GUI.simlatorParts;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ConnectionView extends Line {

    public ConnectionView(Circle fromPort, Circle toPort) {
        NodeView fromNode = (NodeView) fromPort.getParent();
        NodeView toNode   = (NodeView) toPort.getParent();

        startXProperty().bind(fromNode.layoutXProperty().add(fromPort.layoutXProperty()));
        startYProperty().bind(fromNode.layoutYProperty().add(fromPort.layoutYProperty()));

        endXProperty().bind(toNode.layoutXProperty().add(toPort.layoutXProperty()));
        endYProperty().bind(toNode.layoutYProperty().add(toPort.layoutYProperty()));

        setStrokeWidth(2);
        setStroke(Color.WHITE);
        toBack();
    }
}
