package com.jomi.GUI.simlatorParts;

import java.util.ArrayList;
import java.util.List;

import com.jomi.App;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public abstract class NodeView extends StackPane {
    private final Project project;
    private final Node node;

    private final VBox root = new VBox();
    private final HBox header = new HBox();
    private final VBox body = new VBox();
    private final VBox infoArea = new VBox();
    private final Region spacer1 = new Region();
    private final Region spacer2 = new Region();

    private final Button toggleButton = new Button("+");
    private final Label titleLabel = new Label();

    private Circle inputPort;
    private Circle outputPort;

    private double dragOffsetX;
    private double dragOffsetY;

    private boolean expanded = false;


    public NodeView(Node node, Project project) {
        this.node = node;
        this.project = project;

        getStyleClass().add("node-root");

        header.getStyleClass().add("node-header");

        toggleButton.getStyleClass().add("node-toggle");
        toggleButton.setOnAction(e -> toggle());

        //titleLabel.setText(title);
        titleLabel.getStyleClass().add("node-title");

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);


        header.getChildren().addAll(toggleButton, spacer1, titleLabel, spacer2, infoArea);

        body.getStyleClass().add("node-body");
        body.setVisible(false);
        body.setManaged(false);

        root.getChildren().addAll(header, body);
        getChildren().add(root);

        createPorts();

        enableContextMenu();
        enableDragging();
        enablePortConnections();
    }

    private void toggle() {
        expanded = !expanded;
        toggleButton.setText(expanded ? "-" : "+");

        body.setVisible(expanded);
        body.setManaged(expanded);

        if (expanded) {
            body.getChildren().setAll(buildContent());
        }
    }

    protected abstract VBox buildContent();
    
    public Label getTitLabel() {
        return titleLabel;
    }


    private void createPorts() {

        // INPUT PORT
        if (node.getInputPort()) {
            inputPort = new Circle(6, Color.DODGERBLUE);
            inputPort.setCursor(Cursor.HAND);
            inputPort.setPickOnBounds(true);
            inputPort.setMouseTransparent(false);

            inputPort.setOnMousePressed(e -> e.consume());
            inputPort.setOnMouseDragged(e -> e.consume());

            getChildren().add(inputPort);
        }

        // OUTPUT PORT
        if (node.getOutputPort()) {
            outputPort = new Circle(6, Color.ORANGE);
            outputPort.setCursor(Cursor.HAND);
            outputPort.setPickOnBounds(true);
            outputPort.setMouseTransparent(false);

            outputPort.setOnMousePressed(e -> e.consume());
            outputPort.setOnMouseDragged(e -> e.consume());

            getChildren().add(outputPort);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double w = getWidth();
        double headerHeight = header.getHeight();
        double y = headerHeight / 2;

        if (inputPort != null) {
            inputPort.setLayoutX(-15);
            inputPort.setLayoutY(y);
        }

        if (outputPort != null) {
            outputPort.setLayoutX(w + 15);
            outputPort.setLayoutY(y);
        }
    }


    private void enableDragging() {
        setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY) { return; }
            if (e.getTarget() instanceof Circle) { return; }

            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) { return; }
            if (e.getTarget() instanceof Circle) return;

            double newX = e.getSceneX() - dragOffsetX;
            double newY = e.getSceneY() - dragOffsetY;

            setLayoutX(newX);
            setLayoutY(newY);

            node.setxPos(newX);
            node.setyPos(newY);
        });
    }

    private void enableContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteNodeItem = new MenuItem("Delete Node");
        MenuItem editItem = new MenuItem("Edit Node");
        MenuItem deleteConnectionItem = new MenuItem("Delete Connections");

        editItem.setOnAction(e -> editNodeContent());
        deleteNodeItem.setOnAction(e -> deleteNode());
        deleteConnectionItem.setOnAction(e -> deleteConnections());


        menu.getItems().addAll(editItem, deleteNodeItem, deleteConnectionItem);

        setOnContextMenuRequested(e -> {
            menu.show(this, e.getSceneX(), e.getSceneY());
        });
    }

    private void editNodeContent() {
        System.out.println("Edit node content: " + node.getId());
    }

    private void deleteNode() {
        System.out.println("Delete node: " + node.getId());
        project.removeNodeAndConnection(node);
        App.getInstance().getProjectManager().saveProject(project);
        App.getInstance().getGui().showSimulator();
    }

    private void deleteConnections() {
        System.out.println("Delete connections for: " + node.getId());
        project.removeConnectionsOf(node);
        App.getInstance().getGui().showSimulator();
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

                ConnectionView connectionView = new ConnectionView(outputPort, other.getInputPort(), connection, project);

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

    public void setInfo(String text) {
        infoArea.getChildren().clear();

        Label label = new Label(text);
        label.getStyleClass().add("node-info");

        infoArea.getChildren().add(label);
    }

    public void applyRarityColor(String rarity) {
        String color;

        switch (rarity.trim().toLowerCase()) {
            case "normal" -> color = "#3a3a3a";
            case "magic"  -> color = "#4b6cff";
            case "rare"   -> color = "#b8860b";
            default       -> color = "#ff0000";
        }

        header.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 12 12 12 12;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 6 10;"
        );
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
