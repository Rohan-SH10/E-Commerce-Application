package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.util.ResponseStructure;

public interface AuthService {

	public ResponseEntity<String> userRegistration(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(String otp);
}
