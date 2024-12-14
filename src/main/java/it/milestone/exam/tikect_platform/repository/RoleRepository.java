package it.milestone.exam.tikect_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByName(String name);

}
