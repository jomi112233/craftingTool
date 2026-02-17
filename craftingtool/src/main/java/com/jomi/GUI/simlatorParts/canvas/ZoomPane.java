package com.jomi.GUI.simlatorParts.canvas;


import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class ZoomPane extends Pane {

    private double scale = 1.0;

    public ZoomPane(Node content) {
        getChildren().add(content);

        addEventFilter(ScrollEvent.SCROLL, e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            scale *= zoomFactor;

            content.setScaleX(scale);
            content.setScaleY(scale);
        });
    }
}
