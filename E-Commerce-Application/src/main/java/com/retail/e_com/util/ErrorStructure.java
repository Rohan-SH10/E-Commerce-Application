package com.retail.e_com.util;

import org.springframework.stereotype.Component;

@Component
public class ErrorStructure<T> {

	private int errorcode;
	private String errorMessage;
	private T errorData;
	public int getErrorCode() {
		return errorcode;
	}
	public ErrorStructure<T> setErrorCode(int errorcode) {
		this.errorcode = errorcode;
		return this;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public ErrorStructure<T> setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}
	public T getErrorData() {
		return errorData;
	}
	public ErrorStructure<T> setErrorData(T i) {
		this.errorData = i;
		return this;
	}
}