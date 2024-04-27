package com.ecommerce.retail_electronicsapp.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.retail_electronicsapp.requestdto.AuthRequest;
import com.ecommerce.retail_electronicsapp.requestdto.OTPRequest;
import com.ecommerce.retail_electronicsapp.requestdto.UserRequest;
import com.ecommerce.retail_electronicsapp.responsedto.AuthResponse;
import com.ecommerce.retail_electronicsapp.responsedto.UserResponse;
import com.ecommerce.retail_electronicsapp.utility.ResponseStructure;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthService {
	
	ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) throws MessagingException;

	ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> userLogin( AuthRequest authRequest);


	ResponseEntity<SimpleResponseStructure> logout(String accessToken, String refreshToken);

	ResponseEntity<ResponseStructure<AuthResponse>> refreshRequest(String accessToken, String refreshToken);
}
