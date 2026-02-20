package com.jomi;

import com.jomi.GUI.GUILoader;
import com.jomi.Handlers.ConfigLoader;
import com.jomi.Handlers.ProgramLoader;
import com.jomi.Handlers.registry.ProjectManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static App instance;

    private static GUILoader loader;
    private final ProjectManager projectManager = new ProjectManager();

    public static App getInstance() {
        return instance;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void start(Stage stage) {
        instance = this;

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

