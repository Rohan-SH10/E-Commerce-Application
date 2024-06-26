package com.retail.e_com.service;

import com.retail.e_com.requestdto.OtpRequest;
import com.retail.e_com.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;

import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.util.ResponseStructure;

public interface AuthService {

	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OtpRequest otpRequest);
}
