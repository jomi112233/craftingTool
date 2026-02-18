package com.jomi.GUI.simlatorParts.canvas;

import com.jomi.GUI.simlatorParts.ConnectionView;
import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.registry.NodeRegister;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NodeCanvas extends Pane {
    private final Project project;

    private Point2D lastMouse;

    private static final int GRID_SIZE = 25;

    public NodeCanvas(Project project) {
        this.project = project;

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

        // Load nodes
        for (Node node : project.getNodes()) {
            addNode(node);
        }

        // Load connections
        for (Connection connection : project.getConnections()) {
            drawConnection(connection);
        }

    }


    private void drawGrid() {
        Canvas grid = new Canvas(getPrefWidth(), getPrefHeight());
        GraphicsContext graphicsContext = grid.getGraphicsContext2D();

        graphicsContext.setStroke(Color.rgb(255, 255, 255, 0.05));
        graphicsContext.setLineWidth(1);

        for (int x = 0 ; x < getPrefWidth() ; x += GRID_SIZE) {
            graphicsContext.strokeLine(x, 0, x, getPrefHeight());
        }

        for (int y = 0 ; y < getPrefHeight() ; y += GRID_SIZE) {
            
        }

        getChildren().add(grid);
    }

    private NodeView findNodeViewById(String id) {
        for (var child : getChildren()) {
            if (child instanceof NodeView view) {
                if (view.getNode().getId().equals(id)) {
                    return view;
                }
            }
        }
        return null;
    }


    public void drawConnection(Connection c) {
        NodeView fromNode = findNodeViewById(c.getFromNodeId());
        NodeView toNode   = findNodeViewById(c.getToNodeID());

        Circle fromPort = c.isFromIsOutput()
                ? fromNode.getOutputPort()
                : fromNode.getInputPort();

        Circle toPort = c.isToIsInput()
                ? toNode.getInputPort()
                : toNode.getOutputPort();

        ConnectionView line = new ConnectionView(fromPort, toPort);
        getChildren().add(line);
    }


    public void addNode(Node node) {
        NodeView view = NodeRegister.createNodeView(node, project);
        getChildren().add(view);
        view.refresh();
        System.out.println("start node refresh");
    }
}
