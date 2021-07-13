package com.exam.model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
@Table(name = "FILE_LOG")
public class FileLog {

	@Id
	@NotNull
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "TOTAL_COUNT")
	private Long totalCount;
	
	@Column(name = "SUCCESS_COUNT")
	private Long successCount;
	
	@Column(name = "FAILURE_COUNT")
	private Long failureCount;
	
	@Column(name = "STATUS")
	private String status;
	
    @Column(name = "CREATED_BY")
    private String createdBy;

    /// @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    /// @UpdateTimestamp
    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
	
    
	@OneToMany(mappedBy = "fileLogId",cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<FileUploadDetails> fileUploadDtls = new LinkedHashSet<>();
	
	
    public FileLog() {
        this.setCreatedDate(LocalDateTime.now());
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Long successCount) {
		this.successCount = successCount;
	}

	public Long getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
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

	public Set<FileUploadDetails> getFileUploadDtls() {
		return fileUploadDtls;
	}

	public void setFileUploadDtls(Set<FileUploadDetails> fileUploadDtls) {
		this.fileUploadDtls = fileUploadDtls;
	}

	public FileLog(Long id, String fileType, String fileName, Long totalCount, Long successCount, Long failureCount,
			String status, String createdBy, LocalDateTime createdDate, String modifiedBy, LocalDateTime modifiedDate,
			Set<FileUploadDetails> fileUploadDtls) {
		this.id = id;
		this.fileType = fileType;
		this.fileName = fileName;
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.failureCount = failureCount;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.fileUploadDtls = fileUploadDtls;
	}

	@Override
	public String toString() {
		return "FileLog [id=" + id + ", fileType=" + fileType + ", fileName=" + fileName + ", totalCount=" + totalCount
				+ ", successCount=" + successCount + ", failureCount=" + failureCount + ", status=" + status
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", fileUploadDtls=" + fileUploadDtls + "]";
	}

	

	

	

	

    
}
