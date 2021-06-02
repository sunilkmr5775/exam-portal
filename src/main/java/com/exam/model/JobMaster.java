package com.exam.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "JOB_MASTER")
public class JobMaster {

	@Id
	@NotNull
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "JOB_NAME", unique = true)
	private String jobName;

	@NotNull
	@Column(name = "FILE_TYPE")
	private String fileType;

	@NotNull
	@Column(name = "FILE_IN_PATH")
	private String fileInPath;
	
	@NotNull
	@Column(name = "FILE_OUT_PATH")
	private String fileOutPath;


	@NotNull
	@Column(name = "STATUS")
	private String status;


	@NotNull
	@Column(name = "CREATED_BY")
	private String createdBy;

	@NotNull
	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	public JobMaster() {
		this.setCreatedDate(LocalDateTime.now());
		this.setModifiedDate(LocalDateTime.now());
	}

	public JobMaster(Long id, String jobName, String jobType, String fileType, String fileInPath, String fileOutPath,
			String pdfPath, String status, String buId, String createdBy, LocalDateTime createdDate, String modifiedBy,
			LocalDateTime modifiedDate) {
//		super();
		this.id = id;
		this.jobName = jobName;
		this.fileType = fileType;
		this.fileInPath = fileInPath;
		this.fileOutPath = fileOutPath;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileInPath() {
		return fileInPath;
	}

	public void setFileInPath(String fileInPath) {
		this.fileInPath = fileInPath;
	}

	public String getFileOutPath() {
		return fileOutPath;
	}

	public void setFileOutPath(String fileOutPath) {
		this.fileOutPath = fileOutPath;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "JobMaster [id=" + id + ", jobName=" + jobName + ",  fileType=" + fileType
				+ ", fileInPath=" + fileInPath + ", fileOutPath=" + fileOutPath + ", status="
				+ status + ",  createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + "]";
	}

}
