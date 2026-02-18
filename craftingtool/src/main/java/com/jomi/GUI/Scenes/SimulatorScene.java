package com.jomi.GUI.Scenes;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.SideBar;
import com.jomi.GUI.simlatorParts.canvas.NodeCanvas;
import com.jomi.GUI.simlatorParts.canvas.ZoomPane;
import com.jomi.Handlers.Init.project.Project;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class SimulatorScene {

    public Scene createScene() {

        Project project = App.getInstance().getProjectManager().getCurrentProject();
        NodeCanvas canvas = new NodeCanvas(project);

        ZoomPane zoomPane = new ZoomPane(canvas);
        SideBar sidebar = new SideBar(canvas, project);

        canvas.getStylesheets().add(
            getClass().getResource("/styles/nodes.css").toExternalForm()
        );


        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(zoomPane);

        Platform.runLater(() -> {
            canvas.centerOnCanvasCenter(zoomPane.getWidth(), zoomPane.getHeight());
            System.out.println("centring");
            System.out.println("TX = " + canvas.getRootLayer().getTranslateX());
            System.out.println("TY = " + canvas.getRootLayer().getTranslateY());

        });


        Scene scene = new Scene(root);

        return scene;
    }
}
