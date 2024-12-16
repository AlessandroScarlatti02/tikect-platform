package it.milestone.exam.tikect_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Note;
import it.milestone.exam.tikect_platform.model.Ticket;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByTicket(Ticket ticket);
}
