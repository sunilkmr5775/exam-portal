package com.exam.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.exam.model.Quiz;

@Service
public interface QuizService {

	public Quiz addQuiz(Quiz quiz);

	public Quiz updateQuiz(Quiz quiz);

	public Set<Quiz> getQuizzes();

	public Quiz getQuiz(Long quizId);

	public void deleteQuiz(Long quizId);
}
