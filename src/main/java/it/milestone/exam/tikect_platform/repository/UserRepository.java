package it.milestone.exam.tikect_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.User;
import java.util.List;
import it.milestone.exam.tikect_platform.model.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(List<Role> roles);

    List<User> findByMail(String mail);

}
