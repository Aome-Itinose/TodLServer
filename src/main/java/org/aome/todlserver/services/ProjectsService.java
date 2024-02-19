package org.aome.todlserver.services;

import lombok.RequiredArgsConstructor;
import org.aome.todlserver.models.Project;
import org.aome.todlserver.models.User;
import org.aome.todlserver.repositories.ProjectsRepository;
import org.aome.todlserver.security.UsersDetails;
import org.aome.todlserver.util.exceptions.ProjectEditException;
import org.aome.todlserver.util.exceptions.ProjectNotFoundException;
import org.aome.todlserver.util.exceptions.UserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepository projectsRepository;

    private final UsersService usersService;


    @Transactional
    public void createNewProject(Project project) {

        UsersDetails usersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User creatUser = usersService.findByUsername(usersDetails.getUsername());

        creatUser.setRole("ROLE_TEAMLEAD");
        projectsRepository.save(enrich(project, creatUser));
    }

    public boolean projectExist(String name){
        try{
            findByName(name);
        }catch (ProjectNotFoundException e){
            return false;
        }
        return true;
    }

    public Project findByName(String name){
        return projectsRepository.findByName(name).orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    public void setTeamlead(Project editProject, User newUser) {
        if (usersService.userExist(newUser.getUsername())) {
            newUser = usersService.findByUsername(newUser.getUsername());
            if (newUser.getRole().equals("ROLE_SENIOR")) {
                try {
                    Project project = findByName(editProject.getName());

                    UsersDetails currentUsersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    User currentUser = usersService.findByUsername(currentUsersDetails.getUsername());

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

    @Transactional //Todo
    public void addUser(User user){
        UsersDetails usersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = usersService.findByUsername(usersDetails.getUsername()).getMyProject();
        User developer = usersService.findByUsername(user.getUsername());

        if(!project.getDevelopers().contains(developer)){

            project.getDevelopers().add(developer);
        }else{
            throw new ProjectEditException(String.format("%s already is in %s", usersDetails.getUsername(), project.getName()));
        }
    }

    @Transactional
    public void deleteUserFromProject(User user, Project project){
        project = findByName(project.getName());
        user = usersService.findByUsername(user.getUsername());
        if(user.getMyProject()==null) {
            project.getDevelopers().remove(user);
        }else{
            throw new ProjectEditException(String.format("%s is teamlead.", user.getUsername()));
        }
    }

    @Transactional
    public void editProjectName(Project currentProject, Project newProject){
        findByName(currentProject.getName()).setName(newProject.getName());
    }
    private Project enrich(Project project, User creatUser) {
        project.setStatus("Planned");
        project.setTeamLead(creatUser);

        creatUser.getProjects().add(project);
        return project;
    }
}
