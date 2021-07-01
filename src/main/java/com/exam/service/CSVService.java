package com.exam.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exam.dto.CSVHelper;
import com.exam.model.Category;
import com.exam.model.DeveloperTutorial;
import com.exam.model.Quiz;
import com.exam.repository.CategoryRepository;
import com.exam.repository.DeveloperTutorialRepository;
import com.exam.repository.QuizRepository;

@Service
public class CSVService {
  
  @Autowired
  DeveloperTutorialRepository repository;

  @Autowired
  CategoryRepository categoryRepository;
  
  @Autowired
  QuizRepository quizRepository;
  
  public void saveToCategory(MultipartFile file) {
    try {
      List<Category> category = CSVHelper.csvToCategory(file.getInputStream());
      categoryRepository.saveAll(category);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }
  
  public void saveToQuiz(MultipartFile file) {
	    try {
	      List<Quiz> quiz = CSVHelper.csvToQuiz(file.getInputStream(), categoryRepository);
	      quizRepository.saveAll(quiz);
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	  }

  public ByteArrayInputStream load() {
    List<DeveloperTutorial> tutorials = repository.findAll();

    ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
    return in;
  }

  public List<DeveloperTutorial> getAllTutorials() {
    return repository.findAll();
  }
}

