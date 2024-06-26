package com.retail.e_com.controller;

import com.retail.e_com.requestdto.OtpRequest;
import com.retail.e_com.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<SimpleResponseStructure> userRegistration(@RequestBody UserRequest userRequest){
		return authService.userRegistration(userRequest);
	}

	@PostMapping("/verify-email")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(@RequestBody OtpRequest otpRequest){
	return authService.verifyOtp(otpRequest);
	}
}
