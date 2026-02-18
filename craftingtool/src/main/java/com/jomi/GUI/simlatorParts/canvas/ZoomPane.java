package com.jomi.GUI.simlatorParts.canvas;

import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class ZoomPane extends Pane {

    private double scale = 1.0;
    private final NodeCanvas canvas;

    public ZoomPane(NodeCanvas canvas) {
        this.canvas = canvas;
        getChildren().add(canvas);
        /* 
        addEventFilter(ScrollEvent.SCROLL, e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            scale *= zoomFactor;

            canvas.getRootLayer().setScaleX(scale);
            canvas.getRootLayer().setScaleY(scale);

            e.consume();
        });
        */
    }
}

