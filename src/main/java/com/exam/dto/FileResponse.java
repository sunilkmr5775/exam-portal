package com.exam.dto;

public class FileResponse extends BaseResponse {

	private String username;
	private String jobName;
	private String filePath;
	private String fileName;


	public String getUsername() {
		return username;
	}
//
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileResponse(String jobName, String filePath, String fileName) {
		this.jobName = jobName;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	public FileResponse() {
	}

	@Override
	public String toString() {
		return "FileResponse [jobName=" + jobName + ", filePath=" + filePath + ", fileName=" + fileName + "]";
	}

	
	

	
}
