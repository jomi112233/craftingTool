package com.jomi.GUI.simlatorParts.canvas;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
//import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ZoomPane extends Region {

    private final Pane content;   // NodeCanvas rootLayer
    private final NodeCanvas nodeCanvas;

    //private double scale = 1.0;
    private Point2D lastMouse;

    public ZoomPane(NodeCanvas nodeCanvas) {
        this.nodeCanvas = nodeCanvas;
        this.content = nodeCanvas.getRootLayer();
        
        setMinSize(0, 0);
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // viewport clipping
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        getChildren().add(content);

        setupPanning();
        //setupZooming();
        addCenterDot();

    }

    private void setupPanning() {
        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                lastMouse = new Point2D(e.getX(), e.getY());
            }
        });

        setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.MIDDLE && lastMouse != null) {
                double dx = e.getX() - lastMouse.getX();
                double dy = e.getY() - lastMouse.getY();

                content.setTranslateX(content.getTranslateX() + dx);
                content.setTranslateY(content.getTranslateY() + dy);

                lastMouse = new Point2D(e.getX(), e.getY());
            }
        });
    }

    /* private void setupZooming() {
        addEventFilter(ScrollEvent.SCROLL, e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            scale *= zoomFactor;

            content.setScaleX(scale);
            content.setScaleY(scale);

            e.consume();
        });
    } */

    private void addCenterDot() {
        double cx = nodeCanvas.getWorldWidth() / 2;
        double cy = nodeCanvas.getWorldHeight() / 2;

        Circle dot = new Circle(5, Color.RED);
        dot.setLayoutX(cx);
        dot.setLayoutY(cy);

        content.getChildren().add(dot);
    }

}
