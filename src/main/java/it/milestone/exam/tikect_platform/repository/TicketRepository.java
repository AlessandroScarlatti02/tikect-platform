package it.milestone.exam.tikect_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
