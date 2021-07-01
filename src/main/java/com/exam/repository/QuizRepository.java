package com.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.Category;
import com.exam.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

	List<Quiz> findByCategory(Category category);

	List<Quiz> findByActive(Boolean status);
	
	List<Quiz> findByCategoryAndActive(Category category, Boolean status);
}
