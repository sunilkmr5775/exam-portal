package com.exam.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private  String jwttoken;
	private  String status;
	private  String errorCode;
	private  String errorDesc;

	public JwtResponse() {
		this.jwttoken = "";
	}

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public JwtResponse(String jwttoken,String status, String errorCode,String errorDesc) {
		this.jwttoken = jwttoken;
		this.status = status;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
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

	@Override
	public String toString() {
		return "JwtResponse [jwttoken=" + jwttoken + ", status=" + status + ", errorCode=" + errorCode + ", errorDesc="
				+ errorDesc + "]";
	}

	
	
	
}