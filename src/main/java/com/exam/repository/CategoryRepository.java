package com.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findCidByTitle(String categoryTitle);

	Category findByTitle(String categoryTitle);

	Category findAllByTitle(String categoryTitle);

//	Category findcidByTitle(String categoryTitle);

}
