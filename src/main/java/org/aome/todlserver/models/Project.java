package org.aome.todlserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Name should be not empty")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "team_lead", referencedColumnName = "id")
    private User teamLead;

    @ManyToMany
    @JoinTable(name = "Users_Projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> developers;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    public List<User> getDevelopers() {
        if(developers==null){
            return new ArrayList<>();
        }
        return developers;
    }

    public List<Task> getTasks() {
        if(tasks==null){
            return new ArrayList<>();
        }
        return tasks;
    }
}
