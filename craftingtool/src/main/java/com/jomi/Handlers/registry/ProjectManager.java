package com.jomi.Handlers.registry;

import com.jomi.Handlers.Init.project.Project;

public class ProjectManager {

    private Project currentProject;

    public void newProject() {
        currentProject = new Project();
    }

    public void setProject(Project project) {
        currentProject = project;
    }

    public Project getProject() {
        return currentProject;
    }

    public boolean hasproject() {
        return currentProject != null;
    }
}
