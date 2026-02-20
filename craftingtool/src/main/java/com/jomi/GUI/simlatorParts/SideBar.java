package com.jomi.GUI.simlatorParts;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Util.ProjectActions;
import com.jomi.Util.SimulationRunner;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
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

        Button addBasicCurrencyNode = new Button("Basic Currency Node");
        addBasicCurrencyNode.setPrefWidth(120);

        addBasicCurrencyNode.setOnAction(e -> {
            Node node = new Node("basic", 200, 200, true, true);
            project.addNode(node);
            canvas.addNode(node);
        });


        Button addEndNode = new Button("End Node");
        addEndNode.setPrefWidth(120);

        addEndNode.setOnAction(e -> {
            Node node = new Node("end", 200, 200, true, false);
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

        Button runButton = new Button("Run Simulation");
        runButton.setOnAction(e -> {
            try {
                LoadedItem result = SimulationRunner.run(project, canvas.getNodeViewMap());
                System.out.println("Simulation complete!");

                // Optional: show a popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Simulation Complete");
                alert.setContentText("Final item saved to baseItemCompleted.json");
                alert.show();

            } catch (Exception ex) {
                ex.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Simulation Failed");
                alert.setContentText(ex.getMessage());
                alert.show();
            }
        });





        save.setPrefWidth(120);
        load.setPrefWidth(120);

        getChildren().addAll(addNode, addBasicCurrencyNode, addStartNode, addEndNode, save, load, runButton);
    }
}
