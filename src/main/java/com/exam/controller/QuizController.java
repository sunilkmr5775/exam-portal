package com.exam.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.model.Quiz;
import com.exam.service.QuizService;

@RestController
@Component
@RequestMapping("/quiz")
@CrossOrigin("*")
public class QuizController {

	@Autowired
	private QuizService quizService;

//	 ADD QUIZ
	@PostMapping("/")
	public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
		quiz.setModifiedDate(null);
		return ResponseEntity.ok(this.quizService.addQuiz(quiz));

	}

//	 UPDATE QUIZ
	@PutMapping("/")
	public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz quiz) {
		if (quiz.getCreatedDate() == null) {
			quiz.setCreatedDate(LocalDateTime.now());
		}
		return ResponseEntity.ok(this.quizService.updateQuiz(quiz));

	}

//	 GET ALL QUIZZES
	@GetMapping("/")
	public ResponseEntity<?> getQuizzes() {
		return ResponseEntity.ok(this.quizService.getQuizzes());

	}

//	 GET SINGLE QUIZ BY ID
	@GetMapping("/{qid}")
	public Quiz getQuiz(@PathVariable("qid") Long qid) {
		return this.quizService.getQuiz(qid);

	}

//	 DELETE QUIZ BY ID
	@DeleteMapping("/{qid}")
	public void deleteQuiz(@PathVariable("qid") Long qid) {
		this.quizService.deleteQuiz(qid);

	}

}
