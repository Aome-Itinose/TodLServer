package org.aome.todlserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 1, max = 300, message = "Task length should be between 1 and 300.")
    @NotBlank(message = "Task should be not empty.")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private int priority;

    @Column(name = "expiration_at")
    private LocalDateTime expirationAt;

    @Column(name = "created_at")
    @NotNull(message = "'createdAt' IS EMPTY, FUCKING YOU BASTARD.")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "works_who_id", referencedColumnName = "id")
    private User worksWho;


}
