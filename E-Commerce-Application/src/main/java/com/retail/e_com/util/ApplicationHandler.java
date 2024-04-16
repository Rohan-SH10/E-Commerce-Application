package com.retail.e_com.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.e_com.exception.InvalidUserRoleException;
import com.retail.e_com.exception.UserExistedByEmailException;

import lombok.AllArgsConstructor;


@RestControllerAdvice
@AllArgsConstructor
public class ApplicationHandler {

	ErrorStructure<String> errorStructure;
	private ResponseEntity<ErrorStructure<String>> errorResponse(HttpStatus status,String message,String errorData)
	{
		return new ResponseEntity<ErrorStructure<String>>(errorStructure.setErrorCode(status.value())
				.setErrorMessage(message)
				.setErrorData(errorData),status);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> userAlreadyExistByEmailException(UserExistedByEmailException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User Already Exist");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> invalidUserRoleException(InvalidUserRoleException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Please select role either Seller or Customer");
	}
}