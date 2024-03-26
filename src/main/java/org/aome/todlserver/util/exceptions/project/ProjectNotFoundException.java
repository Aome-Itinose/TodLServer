package org.aome.todlserver.util.exceptions.project;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException() {
        super("Project not found");
    }
}
