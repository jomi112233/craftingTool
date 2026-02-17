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

    private final Circle inputPort;
    private final Circle outputPort;

    private double dragOffsetX;
    private double dragOffsetY;

    public NodeView(Node node, Project project) {
        this.node = node;
        this.project = project;

        setLayoutX(node.getxPos());
        setLayoutY(node.getyPos());

        setPrefSize(150, 80);
        setPickOnBounds(true);

        Rectangle bg = new Rectangle(150, 80);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        bg.setFill(Color.web("#3c3f41"));
        bg.setStroke(Color.web("#aaaaaa"));

        Label title = new Label(node.getType());
        title.setTextFill(Color.WHITE);
        title.setLayoutX(10);
        title.setLayoutY(10);
        title.setMouseTransparent(true);

        inputPort = new Circle(6, Color.DODGERBLUE);
        outputPort = new Circle(6, Color.ORANGE);

        inputPort.setLayoutX(0);
        inputPort.setLayoutY(40);
        outputPort.setLayoutX(150);
        outputPort.setLayoutY(40);

        inputPort.setPickOnBounds(true);
        outputPort.setPickOnBounds(true);
        inputPort.setMouseTransparent(false);
        outputPort.setMouseTransparent(false);


        inputPort.setOnMousePressed(e -> e.consume());
        inputPort.setOnMouseDragged(e -> e.consume());

        inputPort.setCursor(Cursor.HAND);
        outputPort.setCursor(Cursor.HAND);

        getChildren().addAll(bg, title, inputPort, outputPort);

        enableDragging();
        portFunctionality(node);
    }

    private void enableDragging() {
        setOnMousePressed(e -> {
            if (e.getTarget() instanceof Circle) {
                return;
            }
            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(e -> {
            if (e.getTarget() instanceof Circle) {
                return;
            }

            double newX = e.getSceneX() - dragOffsetX;
            double newY = e.getSceneY() - dragOffsetY;

            setLayoutX(newX);
            setLayoutY(newY);

            node.setxPos(newX);
            node.setyPos(newY);
        });
    }

    private void portFunctionality(Node node) {
        outputPort.setOnMouseReleased(e -> {
            System.out.println("release working");

            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();

            Pane parent = (Pane) getParent();

            List<NodeView> targets = new ArrayList<>();

            for (var nodent : parent.getChildren()) {

                if (nodent instanceof NodeView other 
                    && other != this
                    && other.isMouseNearInput(mouseX, mouseY, 50)) {
                    targets.add(other);
                }
            }

            for (NodeView other : targets) {

                System.out.println(node.getId() + " Connected to: " + other.node.getId());

                Connection connection = new Connection(node.getId(), other.node.getId());
                project.addConnection(connection);

                ConnectionView connectionView = new ConnectionView(outputPort, other.getInputPort());
                parent.getChildren().add(connectionView);
                connectionView.toBack();

                System.out.println("Connection size " + project.connectionSize());
            }

        });
    }


    public boolean isMouseNearInput(double sceneX, double sceneY, double radius) {
        var bounds = inputPort.localToScene(inputPort.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        double dx = sceneX - centerX;
        double dy = sceneY - centerY;

        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= radius;
    }

    public Circle getInputPort() {
        return inputPort;
    }

    public Circle getOutputPort() {
        return outputPort;
    }
}
