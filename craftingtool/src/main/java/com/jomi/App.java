package com.jomi;

import com.jomi.GUI.GUILoader;
import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.ProgramLoader;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static GUILoader loader;


    @Override
    public void start(Stage stage) {
        ConfigLoader.loadConfig();
        ProgramLoader.loadProgramData();

        loader = new GUILoader(stage);
        loader.showStartingScene();
    }

    public static GUILoader getGui() {
        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
