package com.jomi.GUI.simlatorParts;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.baseNode.NodeView;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Project;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ConnectionView extends Line {

    private final Connection connection;
    private final Project project;

    public ConnectionView(Circle fromPort, Circle toPort, Connection connection, Project project) {
        this.connection = connection;
        this.project = project;

        NodeView fromNode = (NodeView) fromPort.getParent();
        NodeView toNode   = (NodeView) toPort.getParent();

        startXProperty().bind(fromNode.layoutXProperty().add(fromPort.layoutXProperty()));
        startYProperty().bind(fromNode.layoutYProperty().add(fromPort.layoutYProperty()));

        endXProperty().bind(toNode.layoutXProperty().add(toPort.layoutXProperty()));
        endYProperty().bind(toNode.layoutYProperty().add(toPort.layoutYProperty()));

        setStrokeWidth(2);
        setStroke(Color.WHITE);

        enableContextMenu();
        toBack();
    }

    private void enableContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteConnection = new MenuItem("Delete Connection");
        deleteConnection.setOnAction(e -> deleteConnection());

        menu.getItems().add(deleteConnection);

        setOnContextMenuRequested(e ->
            menu.show(this, e.getScreenX(), e.getScreenY())
        );
    }


    private void deleteConnection() {
        System.out.println("Deleting Connection");
        project.removeConnection(connection);
        App.getInstance().getGui().showSimulator();
    }


}
