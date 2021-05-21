package com.exam.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "QUESTION")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "QUES_ID")
	private long quesId;
	
	@Column(name = "CONTENT", length = 5000)
	private String content;
	
	@Column(name = "IMAGE")
	private String image;
	
	@Column(name = "OPTION1")
	private String option1;
	
	@Column(name = "OPTION2")
	private String option2;
	
	@Column(name = "OPTION3")
	private String option3;
	
	@Column(name = "OPTION4")
	private String option4;
	
	@Column(name = "ANSWER")
	private String answer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Quiz quiz;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;
	
	

	public Question() {
	}



	public Question(long quesId, String content, String image, String option1, String option2, String option3,
			String option4, String answer, Quiz quiz, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.quesId = quesId;
		this.content = content;
		this.image = image;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.answer = answer;
		this.quiz = quiz;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	

	
	

}
