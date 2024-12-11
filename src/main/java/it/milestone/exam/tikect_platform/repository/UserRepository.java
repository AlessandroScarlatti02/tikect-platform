package it.milestone.exam.tikect_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
