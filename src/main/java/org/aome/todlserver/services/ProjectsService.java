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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepository projectsRepository;

    private final UsersService usersService;

    @Transactional
    public void createNewProject(Project project) {
        projectsRepository.save(enrich(project));
    }

    public List<Project> findAllByName(String name) {
        List<Project> projects = projectsRepository.findAllByName(name);
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException();
        }
        return projects;
    }

    public Project findByNameAndDescription(String name, String description) {
        return projectsRepository.findByNameAndDescription(name, description).orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    public void setTeamlead(Project editProject, User newUser) {
        if (usersService.userExist(newUser.getUsername())) {
            newUser = usersService.findByUsername(newUser.getUsername());
            if (newUser.getRole().equals("ROLE_ADMIN")) {
                try {

                    Project project = findByNameAndDescription(editProject.getName(), editProject.getDescription());

                    UsersDetails currentUsersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    User currentUser = usersService.findByUsername(currentUsersDetails.getUsername());

                    if (project.getTeamLead().equals(currentUser)) {
                        if (newUser.getMyProject() == null) {
                            newUser.setMyProject(project);
                            project.setTeamLead(newUser);

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

    @Transactional
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
    private Project enrich(Project project) {
        project.setStatus("planned");
        project.setDescription(" ");
        UsersDetails usersDetails = (UsersDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.setTeamLead(usersDetails.getUser());

        return project;
    }
}
