package com.jomi.GUI.Scenes;

import com.jomi.Util.ProjectActions;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartingScreen {

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Button newProject = new Button("New Project"); 
        Button loadProject = new Button("Load Project");

        newProject.setOnAction(e -> ProjectActions.createNewProject());
        loadProject.setOnAction(e -> ProjectActions.loadProject());


        root.getChildren().addAll(newProject, loadProject);
        
        return new Scene(root, 800, 600);
    }
}
