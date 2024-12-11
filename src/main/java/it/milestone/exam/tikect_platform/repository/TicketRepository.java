package it.milestone.exam.tikect_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByObjectContains(String string);

}
