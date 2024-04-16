package com.retail.e_com.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserExistedByEmailException extends RuntimeException{

	private String message;
}
