package com.exam.dto;

import java.util.Arrays;

public class ImageResponse {

	private String fileName;
	private byte[] picByte;
	private String username;
	private String fileType;
	private String imageType;
	private String filePath;
	private String status;
	private String errorCode;
	private String errorDescription;
	private boolean profilePicStatus;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	
	
	public boolean isProfilePicStatus() {
		return profilePicStatus;
	}

	public void setProfilePicStatus(boolean profilePicStatus) {
		this.profilePicStatus = profilePicStatus;
	}

	public ImageResponse() {

	}

	@Override
	public String toString() {
		return "ImageResponse [fileName=" + fileName + ", picByte=" + Arrays.toString(picByte) + ", username="
				+ username + ", fileType=" + fileType + ", imageType=" + imageType + ", filePath=" + filePath
				+ ", status=" + status + ", errorCode=" + errorCode + ", errorDescription=" + errorDescription
				+ ", profilePicStatus=" + profilePicStatus + "]";
	}

	

}