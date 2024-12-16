package it.milestone.exam.tikect_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Operator;
import it.milestone.exam.tikect_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByObjectContains(String string);

    List<Ticket> findByOperator(Operator operator);

    List<Ticket> findByOperatorUserUsername(String username);

    List<Ticket> findByState(String state);

    List<Ticket> findByCategoryName(String category);

}
