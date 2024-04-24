package com.ecommerce.retail_electronicsapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountAceessRequestDeniedException extends RuntimeException {

	private String message;
}
