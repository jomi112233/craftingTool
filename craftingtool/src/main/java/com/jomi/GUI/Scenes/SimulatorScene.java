package com.jomi.GUI.Scenes;

import com.jomi.GUI.simlatorParts.SideBar;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.GUI.simlatorParts.canvas.ZoomPane;
import com.jomi.Handlers.registry.ProjectManager;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class SimulatorScene {

    public Scene createScene() {

        NodeCanvas canvas = new NodeCanvas();
        ProjectManager.getProject().getNodes();

        ZoomPane zoomPane = new ZoomPane(canvas);


        SideBar sidebar = new SideBar(canvas);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(zoomPane);

        return new Scene(root);
    }
}

