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
        NodeCanvas nodeCanvas = new NodeCanvas(project);
        ZoomPane zoomPane = new ZoomPane(nodeCanvas);

        zoomPane.setStyle(
            "-fx-background-color: #2b2b2b;");
        System.out.println("ZoomPane size = " + zoomPane.getWidth() + " x " + zoomPane.getHeight());

        // ⭐ Force CSS to re-render after layout
        zoomPane.widthProperty().addListener((obs, oldV, newV) -> zoomPane.applyCss());
        zoomPane.heightProperty().addListener((obs, oldV, newV) -> zoomPane.applyCss());

        System.out.println("ZoomPane size = " + zoomPane.getWidth() + " x " + zoomPane.getHeight());


        SideBar sidebar = new SideBar(nodeCanvas, project); // adjust ctor if needed

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(zoomPane);

        Scene scene = new Scene(root);


        scene.getStylesheets().add(
            getClass().getResource("/styles/nodes.css").toExternalForm()
        );

        return scene;
    }
}