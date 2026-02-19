package com.jomi.GUI.simlatorParts;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Util.ProjectActions;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SideBar extends VBox {
    private final Project project;


    public SideBar(NodeCanvas canvas, Project project) {
        this.project = project;

        setPadding(new Insets(10));
        setSpacing(10);
        setStyle("-fx-background-color: #333333;");

        Button addNode = new Button("Basic Node");
        addNode.setPrefWidth(120);

        addNode.setOnAction(e -> {
            Node node = new Node("new node", 200, 200, true, true);
            canvas.addNode(node);
            this.project.addNode(node);
            System.out.println("Total Nodes: " + project.getNodeSize());
            System.out.println("node IDs: " + project.getAllNodeIds());
        });


        Button addStartNode = new Button("Start Node");
        addStartNode.setPrefWidth(120);

        addStartNode.setOnAction(e -> {
            Node node = new Node("start", 200, 200, false, true);
            project.addNode(node);
            canvas.addNode(node);
        });

        Button addEndNode = new Button("end Node");
        addEndNode.setPrefWidth(120);

        addEndNode.setOnAction(e -> {
            Node node = new Node("End", 200, 200, true, false);
            project.addNode(node);
            canvas.addNode(node);
        });




        Button save = new Button("Save");
        save.setOnAction(e -> {
            App.getInstance().getProjectManager().saveProject(project);
        });

        
        Button load = new Button("Load");
        load.setOnAction(e -> {
            ProjectActions.loadProject();
        });




        save.setPrefWidth(120);
        load.setPrefWidth(120);

        getChildren().addAll(addNode, addStartNode, addEndNode, save, load);
    }
}
