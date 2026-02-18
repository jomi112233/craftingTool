package com.jomi.GUI.Scenes;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.SideBar;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.GUI.simlatorParts.canvas.ZoomPane;
import com.jomi.Handlers.Init.project.Project;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class SimulatorScene {

    public Scene createScene() {

        Project project = App.getInstance().getProjectManager().getCurrentProject();
        NodeCanvas canvas = new NodeCanvas(project);


        ZoomPane zoomPane = new ZoomPane(canvas);


        SideBar sidebar = new SideBar(canvas, project);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(zoomPane);

        return new Scene(root);
    }
}

