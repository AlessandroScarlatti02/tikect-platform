package it.milestone.exam.tikect_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Operator;
import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.model.User;

public interface OperatorRepository extends JpaRepository<Operator, Long> {

    List<Operator> findByState(Boolean state);

    Operator findByUser(User user);

    Operator findByUserUsername(String username);

    List<Ticket> findByTicketsStateNot(String state);

}
