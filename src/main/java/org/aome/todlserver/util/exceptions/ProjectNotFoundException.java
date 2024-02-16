package org.aome.todlserver.util.exceptions;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException() {
        super("Project not found");
    }
}
