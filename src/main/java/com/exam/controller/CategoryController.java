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

import com.exam.model.Category;
import com.exam.service.CategoryService;

@RestController
@Component
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

//	 ADD CATEGORY
	@PostMapping("/")
	public ResponseEntity<?> addCategory(@RequestBody Category category) {

		Category category1 = this.categoryService.addCategory(category);
		category1.setModifiedDate(null);
		return ResponseEntity.ok(category1);

	}

//	 UPDATE CATEGORY
	@PutMapping("/")
	public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
		category.setModifiedDate(LocalDateTime.now());
		Category category2 = this.categoryService.updateCategory(category);
		return ResponseEntity.ok(category2);

	}

//	 GET ALL CATEGORIES
	@GetMapping("/")
	public Set<Category> getCategories() {
		return new HashSet<>(this.categoryService.getCategories());

	}

//	 GET CATEGORY BY ID
	@GetMapping("/{categoryId}")
	public Category getCategory(@PathVariable Long categoryId) {
		return this.categoryService.getCategory(categoryId);

	}

//	 DELETE CATEGORY BY ID
	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) {
		this.categoryService.deleteCategory(categoryId);

	}

}
