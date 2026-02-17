package com.jomi.GUI.Scenes;

import com.jomi.GUI.simlatorParts.SideBar;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.GUI.simlatorParts.canvas.ZoomPane;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class SimulatorScene {

    public Scene createScene() {

        NodeCanvas canvas = new NodeCanvas();

        ZoomPane zoomPane = new ZoomPane(canvas);


        SideBar sidebar = new SideBar(canvas);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(zoomPane);

        return new Scene(root);
    }
}

