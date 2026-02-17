package com.jomi.Util;

import com.jomi.App;

public class ProjectActions {


    public static void createNewProject() {
        System.out.println("Creating new project...");

        try {
            App.getInstance().getProjectManager().newProject();
            App.getInstance().getGui().showSimulator();

            System.out.println("New project created successfully.");

        } catch (Exception e) {
            System.err.println("Failed to create new project: " + e.getMessage());
        }
    }



    public static void loadProject() {
        System.out.println("Loading existing project...");
    }
}
