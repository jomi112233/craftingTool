package com.jomi.GUI.simlatorParts.canvas;

import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NodeCanvas extends Pane {
    
    private Point2D lastMouse;

    Project project = new Project();

    private static final int GRID_SIZE = 25;

    public NodeCanvas() {
        setPrefSize(4000, 2000);
        setStyle("-fx-background-color: #2b2b2b;");
        drawGrid();

        // Panning
        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                lastMouse = new Point2D(e.getSceneX(), e.getSceneY());
            }
        });

        setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                double dx = e.getSceneX() - lastMouse.getX();
                double dy = e.getSceneY() - lastMouse.getY();

                setTranslateX(getTranslateX() + dx);
                setTranslateY(getTranslateY() + dy);

                lastMouse = new Point2D(e.getSceneX(), e.getSceneY());
            }
        });

    }

    private void drawGrid() {
        Canvas grid = new Canvas(getPrefWidth(), getPrefHeight());
        GraphicsContext graphicsContext = grid.getGraphicsContext2D();

        graphicsContext.setStroke(Color.rgb(255, 255, 255, 0.05));
        graphicsContext.setLineWidth(1);

        for (int x = 0; x < getPrefWidth(); x += GRID_SIZE) {
            graphicsContext.strokeLine(x, 0, x, getPrefHeight());
        }

        for (int y = 0; y < getPrefHeight(); y += GRID_SIZE) {
            graphicsContext.strokeLine(0, y, getPrefWidth(), y);
        }

        getChildren().add(grid);
    }


    public void addNode(Node node) {
        NodeView view = new NodeView(node, project);
        getChildren().add(view);
    }
}
