package it.milestone.exam.tikect_platform.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Can't be blank or null")
    private String object;

    @NotBlank(message = "Can't be blank or null")
    private String text;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "ticket")
    private List<Note> notes;

    @NotBlank(message = "Can't be blank or null")
    private String state = "To do";

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
