package com.exam.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProfilePicRequest {

	private String username;
	private MultipartFile file;
	private String jobName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}



	
}
