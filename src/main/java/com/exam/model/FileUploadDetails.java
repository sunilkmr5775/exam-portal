package com.exam.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "FILE_UPLD_DTLS")
public class FileUploadDetails {

	@Id
	@NotNull
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "FILE_TYPE")
	private String fileType;

	@Column(name = "FILE_NAME")
	private String fileName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private FileLog fileLogId;

	public FileLog getFileLogId() {
		return fileLogId;
	}

	public void setFileLogId(FileLog fileLogId) {
		this.fileLogId = fileLogId;
	}




	@Column(name = "S_NO")
	private String sNo;
	
	@Column(name = "RECORD")
	private String record;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ERROR_CODE")
	private String errorCode;
	
	@Column(name = "ERROR_DESC")
	private String errorDesc;
	
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
	
    public FileUploadDetails() {
        this.setCreatedDate(LocalDateTime.now());
    }

	

	public FileUploadDetails(Long id, String fileType, String fileName, FileLog fileLogId, String sNo, String record,
			String status, String errorCode, String errorDesc, String createdBy, LocalDateTime createdDate,
			String modifiedBy, LocalDateTime modifiedDate) {
//		super();
		this.id = id;
		this.fileType = fileType;
		this.fileName = fileName;
		this.fileLogId = fileLogId;
		this.sNo = sNo;
		this.record = record;
		this.status = status;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
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

	public String getsNo() {
		return sNo;
	}

	public void setsNo(String sNo) {
		this.sNo = sNo;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
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
		return "FileUploadDetails [id=" + id + ", fileType=" + fileType + ", fileName=" + fileName + ", fileLogId="
				+ fileLogId + ", sNo=" + sNo + ", record=" + record + ", status=" + status + ", errorCode=" + errorCode
				+ ", errorDesc=" + errorDesc + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + "]";
	}
	
	
	


    
}
