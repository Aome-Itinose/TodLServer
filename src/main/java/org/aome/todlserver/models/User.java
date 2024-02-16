package org.aome.todlserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    @NotBlank(message = "Username should be not empty")
    @Size(min = 2, max = 100, message = "Username length should be between 2 and 100")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "gmail")
    private String gmail;

    @Column(name = "role")
    private String role;

    @ManyToMany
    @JoinTable(name = "Users_Projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;

    @OneToMany(mappedBy = "worksWho")
    private List<Task> tasks;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "teamLead")
    private Project myProject;

    public List<Project> getProjects() {
        if(projects==null)
            return new ArrayList<>();
        return projects;
    }

    public List<Task> getTasks() {
        if(tasks==null)
            return new ArrayList<>();
        return tasks;
    }
}
