package com.jomi.Handlers.registry;

import com.jomi.Handlers.Init.project.Project;

public class ProjectManager {

    private static Project currentProject;

    public static void newProject() {
        currentProject = new Project();
    }

    public static void setProject(Project project) {
        currentProject = project;
    }

    public static Project getProject() {
        return currentProject;
    }

    public static boolean hasproject() {
        return currentProject != null;
    }
}
