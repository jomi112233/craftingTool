package com.jomi.GUI.simlatorParts;

import java.util.ArrayList;
import java.util.List;

import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class NodeView extends Pane {

    private final Project project;
    private final Node node;

    private Label titleLabel;

    private Circle inputPort;
    private Circle outputPort;

    private double dragOffsetX;
    private double dragOffsetY;

    public NodeView(Node node, Project project) {
        this.node = node;
        this.project = project;

        // Title label
        titleLabel = new Label(node.getType());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(10);

        setLayoutX(node.getxPos());
        setLayoutY(node.getyPos());

        setPrefSize(150, 80);
        setPickOnBounds(true);

        // Background
        Rectangle bg = new Rectangle(150, 240);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        bg.setFill(Color.web("#3c3f41"));
        bg.setStroke(Color.web("#aaaaaa"));

        getChildren().addAll(bg, titleLabel);

        // Create ports based on node flags
        createPorts();

        enableDragging();
        enablePortConnections();
    }

    private void createPorts() {

        // INPUT PORT
        if (node.getInputPort()) {
            inputPort = new Circle(6, Color.DODGERBLUE);
            inputPort.setLayoutX(0);
            inputPort.setLayoutY(40);
            inputPort.setCursor(Cursor.HAND);
            inputPort.setPickOnBounds(true);
            inputPort.setMouseTransparent(false);

            // Prevent dragging the node when clicking port
            inputPort.setOnMousePressed(e -> e.consume());
            inputPort.setOnMouseDragged(e -> e.consume());

            getChildren().add(inputPort);
        }

        // OUTPUT PORT
        if (node.getOutputPort()) {
            outputPort = new Circle(6, Color.ORANGE);
            outputPort.setLayoutX(150);
            outputPort.setLayoutY(40);
            outputPort.setCursor(Cursor.HAND);
            outputPort.setPickOnBounds(true);
            outputPort.setMouseTransparent(false);

            // Prevent dragging the node when clicking port
            outputPort.setOnMousePressed(e -> e.consume());
            outputPort.setOnMouseDragged(e -> e.consume());

            getChildren().add(outputPort);
        }
    }

    private void enableDragging() {
        setOnMousePressed(e -> {
            if (e.getTarget() instanceof Circle) return;

            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(e -> {
            if (e.getTarget() instanceof Circle) return;

            double newX = e.getSceneX() - dragOffsetX;
            double newY = e.getSceneY() - dragOffsetY;

            setLayoutX(newX);
            setLayoutY(newY);

            node.setxPos(newX);
            node.setyPos(newY);
        });
    }

    private void enablePortConnections() {

        // If no output port, this node cannot start a connection
        if (outputPort == null) return;

        outputPort.setOnMouseReleased(e -> {

            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();

            Pane parent = (Pane) getParent();
            List<NodeView> targets = new ArrayList<>();

            for (var nodent : parent.getChildren()) {
                if (nodent instanceof NodeView other
                    && other != this
                    && other.inputPort != null
                    && other.isMouseNearInput(mouseX, mouseY, 50)) {

                    targets.add(other);
                }
            }

            for (NodeView other : targets) {

                Connection connection = new Connection(
                        node.getId(),
                        true,   
                        other.node.getId(),
                        true
                );

                project.addConnection(connection);

                ConnectionView connectionView =
                        new ConnectionView(outputPort, other.getInputPort());

                parent.getChildren().add(connectionView);
                connectionView.toBack();
            }
        });
    }

    public boolean isMouseNearInput(double sceneX, double sceneY, double radius) {
        if (inputPort == null) return false;

        var bounds = inputPort.localToScene(inputPort.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        double dx = sceneX - centerX;
        double dy = sceneY - centerY;

        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    public Circle getInputPort() {
        return inputPort;
    }

    public Circle getOutputPort() {
        return outputPort;
    }

    public Node getNode() {
        return node;
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void refresh() {
        
    }

    protected Project getProject() {
    return project;
    }

    protected Label getTitleLabel() {
        return titleLabel;
    }


}
