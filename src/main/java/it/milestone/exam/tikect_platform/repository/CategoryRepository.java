package it.milestone.exam.tikect_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.exam.tikect_platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
