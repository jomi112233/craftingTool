package com.jomi.GUI;

import com.jomi.GUI.Scenes.SimulatorScene;
import com.jomi.GUI.Scenes.StartingScreen;
import javafx.stage.Stage;

public class GUILoader {

    private final Stage primaryStage;

    public GUILoader(Stage stage) {
        this.primaryStage = stage;
        stage.setMaximized(true);
    }

    public void showStartingScene() {
        StartingScreen startingScreen = new StartingScreen();
        primaryStage.setScene(startingScreen.createScene());
        primaryStage.show();
    }

    public void showSimulator() {
        SimulatorScene simulatorScene = new SimulatorScene();
        primaryStage.setScene(simulatorScene.createScene());
        primaryStage.show();
    }
}
