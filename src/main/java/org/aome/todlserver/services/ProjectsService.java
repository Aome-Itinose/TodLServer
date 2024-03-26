package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.User;
import org.aome.todlserver.repositories.ProjectsRepository;
import org.aome.todlserver.util.exceptions.project.ProjectEditException;
import org.aome.todlserver.util.exceptions.project.ProjectNotFoundException;
import org.aome.todlserver.util.exceptions.user.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepository projectsRepository;

    private final UsersService usersService;


    public Project findProjectByName(String name) {
        return projectsRepository.findByName(name).orElseThrow(ProjectNotFoundException::new);
    }
    public Project findProjectById(int id) {
        Project project = projectsRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        if(project.getDevelopers().contains(usersService.findAuthenticatedUser())){
            return project;
        }
        throw new ProjectNotFoundException();
    }
    public boolean isProjectExist(String name) {
        try {
            findProjectByName(name);
        } catch (ProjectNotFoundException e) {
            return false;
        }
        return true;
    }
    @Transactional
    public void createNewProject(Project project) {
        User creatUser = usersService.findAuthenticatedUser();

        creatUser.setRole("ROLE_TEAMLEAD");
        projectsRepository.save(enrich(project, creatUser));
    }
    @Transactional
    public void editProjectName(Project newProject) {
        Project currentProject = usersService.findAuthenticatedUser().getMyProject();
        findProjectByName(currentProject.getName()).setName(newProject.getName());
    }
    @Transactional
    public void addUser(User user) {
        Project project = usersService.findAuthenticatedUser().getMyProject();
        User developer = usersService.findByUsername(user.getUsername());

        if (!project.getDevelopers().contains(developer)) {
            project.getDevelopers().add(developer);
        } else {
            throw new ProjectEditException(String.format("%s already is in %s", developer.getUsername(), project.getName()));
        }
    }
    @Transactional
    public void deleteUserFromProject(User user) {
        Project project = usersService.findAuthenticatedUser().getMyProject();
        project = findProjectByName(project.getName());
        user = usersService.findByUsername(user.getUsername());
        if (user.getMyProject() == null) {
            project.getDevelopers().remove(user);
        } else {
            throw new ProjectEditException(String.format("%s is teamlead.", user.getUsername()));
        }
    }
    @Transactional
    public void setTeamlead(Project editProject, User newUser) {
        if (usersService.userExist(newUser.getUsername())) {
            newUser = usersService.findByUsername(newUser.getUsername());
            if (newUser.getRole().equals("ROLE_SENIOR")) {
                try {
                    Project project = findProjectByName(editProject.getName());

                    User currentUser = usersService.findAuthenticatedUser();

                    if (project.getTeamLead().getUsername().equals(currentUser.getUsername())) {
                        if (newUser.getMyProject() == null) {
                            newUser.setMyProject(project);
                            project.setTeamLead(newUser);

                            newUser.setRole("ROLE_TEAMLEAD");
                            currentUser.setRole("ROLE_SENIOR");

                            project.getDevelopers().add(newUser);
                        } else {
                            throw new ProjectEditException(String.format("%s is already has project", newUser.getUsername()));
                        }
                    } else {
                        throw new ProjectEditException("You aren't teamlead");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                throw new ProjectEditException(String.format("%s isn't admin.", newUser.getUsername()));
            }
        } else {
            throw new UserNotFoundException();
        }
    }


    private Project enrich(Project project, User creatUser) {
        project.setStatus("Planned");
        project.setTeamLead(creatUser);

        creatUser.getProjects().add(project);
        return project;
    }
}
