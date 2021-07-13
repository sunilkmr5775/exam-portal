package com.exam.dto;

public class BaseResponse {
	private String status;
	private String errorCode;
	private String errorDescription;
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
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	@Override
	public String toString() {
		return "BaseResponse [status=" + status + ", errorCode=" + errorCode + ", errorDescription=" + errorDescription
				+ "]";
	}
	
	
	
	

}
