package com.jomi.GUI.simlatorParts;

import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SideBar extends VBox {
    Project project = new Project();


    public SideBar(NodeCanvas canvas) {
        setPadding(new Insets(10));
        setSpacing(10);
        setStyle("-fx-background-color: #333333;");

        Button addNode = new Button("Basic Node");
        addNode.setPrefWidth(120);

        addNode.setOnAction(e -> {
            Node node = new Node("new node", 200, 200, true, true);
            canvas.addNode(node);
            project.addNode(node);
            System.out.println("Total Nodes: " + project.getNodeSize());
            System.out.println("node IDs: " + project.getAllNodeIds());
        });


        Button save = new Button("Save");
        Button load = new Button("Load");


        save.setPrefWidth(120);
        load.setPrefWidth(120);

        getChildren().addAll(addNode, save, load);
    }
}
