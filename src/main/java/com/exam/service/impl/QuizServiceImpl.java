package com.exam.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.constant.StatusConstant;
import com.exam.model.Category;
import com.exam.model.Quiz;
import com.exam.repository.QuizRepository;
import com.exam.service.QuizService;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizRepository quizRepository;

	@Override
	public Quiz addQuiz(Quiz quiz) {
		
		quiz.setCreatedDate(LocalDateTime.now());
		quiz.setModifiedDate(null);
		return this.quizRepository.save(quiz);
	}

	@Override
	public Quiz updateQuiz(Quiz quiz) {
		return this.quizRepository.save(quiz);
	}

	@Override
	public Set<Quiz> getQuizzes() {
		return new LinkedHashSet<>(this.quizRepository.findAll());
	}

	@Override
	public Quiz getQuiz(Long quizId) {
		Quiz quiz = this.quizRepository.findById(quizId).get();
		return quiz;
	}

	@Override
	public void deleteQuiz(Long quizId) {
		this.quizRepository.deleteById(quizId);

	}

	@Override
	public List<Quiz> getQuizzesOfCategory(Category category) {
		// TODO Auto-generated method stub
		return this.quizRepository.findByCategory(category);
	}

	
	
//	Get Active Quizzes
	@Override
	public List<Quiz> getActiveQuizzes() {

		return this.quizRepository.findByActive(StatusConstant.STATUS_ACTIVE1);
	}

	@Override
	public List<Quiz> getActiveQuizzesOfCategory(Category category) {
		// TODO Auto-generated method stub
		return this.quizRepository.findByCategoryAndActive(category, StatusConstant.STATUS_ACTIVE1);
	}
	
}
