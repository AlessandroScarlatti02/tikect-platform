package it.milestone.exam.tikect_platform.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Can't be blank or null")
    private String name;

    @NotBlank(message = "Can't be blank or null")
    private String mail;

    @NotBlank(message = "Can't be blank or null")
    private String password;

    @NotBlank(message = "Can't be blank or null")
    private String state;

    @OneToMany(mappedBy = "ticket")
    private List<Ticket> tickets;

    // @ManyToMany(fetch = FetchType.EAGER)
    // private List<Role> role;
}
