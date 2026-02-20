package com.jomi.GUI.simlatorParts.canvas;

import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import java.util.HashMap;
import java.util.Map;

import com.jomi.GUI.simlatorParts.ConnectionView;
import com.jomi.GUI.simlatorParts.NodeView;
import com.jomi.Handlers.registry.NodeRegister;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NodeCanvas {

    private static final int GRID_SIZE = 25;

    private final Project project;

    private final Pane rootLayer = new Pane();
    private final Pane gridLayer = new Pane();
    private final Pane connectionLayer = new Pane();
    private final Pane nodeLayer = new Pane();
    private final Pane simulatorObjectLayer = new Pane();

    private final Map<String, NodeView> nodeViewMap = new HashMap<>();

    private static double width = 4000;
    private static double height = 2000;

    public NodeCanvas(Project project) {
        this.project = project;

        rootLayer.setPrefSize(width, height);
        rootLayer.getChildren().addAll(
            gridLayer,
            connectionLayer,
            nodeLayer,
            simulatorObjectLayer
        );

        drawGrid();

        // Load nodes
        for (Node node : project.getNodes()) {
            addNode(node);
        }

        // Load connections
        for (Connection c : project.getConnections()) {
            drawConnection(c);
        }
    }
    

    public Pane getRootLayer() {
        return rootLayer;
    }

    public double getWorldWidth() {
        return width;
    }

    public double getWorldHeight() {
        return height;
    }

    //not used really currently
    private void drawGrid() {
        Canvas grid = new Canvas(width, height);
        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.rgb(255, 255, 255, 0));
        gc.setLineWidth(1);

        for (int x = 0; x < width; x += GRID_SIZE) {
            gc.strokeLine(x, 0, x, height);
        }

        for (int y = 0; y < height; y += GRID_SIZE) {
            gc.strokeLine(0, y, width, y);
        }

        grid.setMouseTransparent(true);
        gridLayer.getChildren().add(grid);
    }

    private NodeView findNodeViewById(String id) {
        for (var child : nodeLayer.getChildren()) {
            if (child instanceof NodeView view && view.getNode().getId().equals(id)) {
                return view;
            }
        }
        return null;
    }

    public void drawConnection(Connection c) {
        NodeView fromNode = findNodeViewById(c.getFromNodeId());
        NodeView toNode   = findNodeViewById(c.getToNodeID());

        if (fromNode == null || toNode == null) {
            return;
        }

        var fromPort = c.isFromIsOutput()
                ? fromNode.getOutputPort()
                : fromNode.getInputPort();

        var toPort = c.isToIsInput()
                ? toNode.getInputPort()
                : toNode.getOutputPort();

        ConnectionView line = new ConnectionView(fromPort, toPort, c, project);
        connectionLayer.getChildren().add(line);
    }

    public void addNode(Node node) {
        NodeView view = NodeRegister.createNodeView(node, project);
        nodeLayer.getChildren().add(view);

        nodeViewMap.put(node.getId(), view);   // <-- FIX

        Platform.runLater(() -> {
            view.setLayoutX(node.getxPos());
            view.setLayoutY(node.getyPos());
        });
    }


    public void addSimulatorObject(javafx.scene.Node obj) {
        simulatorObjectLayer.getChildren().add(obj);
    }

    public Map<String, NodeView> getNodeViewMap() {
        return nodeViewMap;
    }

}
