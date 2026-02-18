package com.jomi.GUI.simlatorParts.canvas;

import com.jomi.GUI.simlatorParts.ConnectionView;
import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.registry.NodeRegister;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NodeCanvas extends Pane {

    private final Project project;

    // ⭐ Your desired layer order (top → bottom)
    private final Pane simulatorObjectLayer = new Pane();
    private final Pane nodeLayer = new Pane();
    private final Pane connectionLayer = new Pane();
    private final Pane gridLayer = new Pane();

    // ⭐ Root layer that ZoomPane will scale
    private final Pane rootLayer = new Pane();

    private Point2D lastMouse;
    private static final int GRID_SIZE = 25;

    public NodeCanvas(Project project) {
        this.project = project;


        setPrefSize(4000, 2000);
        setStyle("-fx-background-color: #2b2b2b;");

        rootLayer.getChildren().addAll(
            gridLayer,
            connectionLayer,
            nodeLayer,
            simulatorObjectLayer
        );

        rootLayer.setPrefSize(getPrefWidth(), getPrefHeight());

        addCenterDot();

        getChildren().add(rootLayer);
        drawGrid();
        setupPanning();


        // Load nodes
        for (Node node : project.getNodes()) {
            addNode(node);
        }

        // Load connections
        for (Connection c : project.getConnections()) {
            drawConnection(c);
        }

    }

    private void addCenterDot() {
        double centerX = getPrefWidth() / 2;
        double centerY = getPrefHeight() / 2;

        javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(5, Color.RED);
        dot.setLayoutX(centerX);
        dot.setLayoutY(centerY);

        // Put it on the top-most layer so it's always visible
        simulatorObjectLayer.getChildren().add(dot);
    }

    public void centerOnCanvasCenter(double viewportWidth, double viewportHeight) {
        double canvasCenterX = getPrefWidth() / 2;
        double canvasCenterY = getPrefHeight() / 2;

        double viewportCenterX = viewportWidth / 2;
        double viewportCenterY = viewportHeight / 2;

        rootLayer.setTranslateX(viewportCenterX - canvasCenterX);
        rootLayer.setTranslateY(viewportCenterY - canvasCenterY);
    }





    public Pane getRootLayer() {
        return rootLayer;
    }

    private void drawGrid() {
        Canvas grid = new Canvas(getPrefWidth(), getPrefHeight());
        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.rgb(255, 255, 255, 0.05));
        gc.setLineWidth(1);

        for (int x = 0; x < getPrefWidth(); x += GRID_SIZE) {
            gc.strokeLine(x, 0, x, getPrefHeight());
        }

        for (int y = 0; y < getPrefHeight(); y += GRID_SIZE) {
            gc.strokeLine(0, y, getPrefWidth(), y);
        }

        grid.setMouseTransparent(true);
        gridLayer.getChildren().add(grid);
    }

    private void setupPanning() {
        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                lastMouse = new Point2D(e.getSceneX(), e.getSceneY());
            }
        });

        setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                double dx = e.getSceneX() - lastMouse.getX();
                double dy = e.getSceneY() - lastMouse.getY();

                rootLayer.setTranslateX(rootLayer.getTranslateX() + dx);
                rootLayer.setTranslateY(rootLayer.getTranslateY() + dy);

                lastMouse = new Point2D(e.getSceneX(), e.getSceneY());
            }
        });
    }




    private NodeView findNodeViewById(String id) {
        for (var child : nodeLayer.getChildren()) {
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

        ConnectionView line = new ConnectionView(fromPort, toPort, c, project);
        connectionLayer.getChildren().add(line);
    }

    public void addNode(Node node) {
        NodeView view = NodeRegister.createNodeView(node, project);
        view.setManaged(true);
        nodeLayer.getChildren().add(view);

        Platform.runLater(() -> {
            view.setLayoutX(node.getxPos());
            view.setLayoutY(node.getyPos());
        });
    }

    public void addSimulatorObject(javafx.scene.Node obj) {
        simulatorObjectLayer.getChildren().add(obj);
    }
}
