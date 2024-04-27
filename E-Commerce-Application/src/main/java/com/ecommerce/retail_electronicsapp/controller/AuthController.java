package com.ecommerce.retail_electronicsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.retail_electronicsapp.jwt.JwtFilter;
import com.ecommerce.retail_electronicsapp.jwt.JwtService;
import com.ecommerce.retail_electronicsapp.requestdto.OTPRequest;
import com.ecommerce.retail_electronicsapp.requestdto.AuthRequest;
import com.ecommerce.retail_electronicsapp.requestdto.UserRequest;
import com.ecommerce.retail_electronicsapp.responsedto.AuthResponse;
import com.ecommerce.retail_electronicsapp.responsedto.UserResponse;
import com.ecommerce.retail_electronicsapp.service.AuthService;
import com.ecommerce.retail_electronicsapp.utility.ResponseStructure;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/re-v1")
//@CrossOrigin(origin="http://localhost:5173")
public class AuthController {

	private AuthService authService;
	private JwtFilter jwtFilter;
	
	@PostMapping("/register")
	public ResponseEntity<SimpleResponseStructure> userRegistration(@RequestBody @Valid UserRequest userRequest) throws MessagingException {
		return authService.userRegistration(userRequest);
	}
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(@RequestBody @Valid AuthRequest authRequest) throws MessagingException {
		return authService.userLogin(authRequest);
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(@RequestBody OTPRequest otpRequest){
		return authService.verifyOTP(otpRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(required = false,value="at") String accessToken,@CookieValue(required = false,value="rt") String refreshToken){
		return authService.logout(accessToken,refreshToken);
	}
	
	@GetMapping("refresh-request")
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshRequest(@CookieValue(required = false,value="at") String accessToken,@CookieValue(required = false,value="rt") String refreshToken){
		return authService.refreshRequest(accessToken,refreshToken);
	}
	
	
//	@PreAuthorize("CUSTOMER")
//	@GetMapping("/test")
//	public String test() {
//		return "Hello Boss";
//	}
	
//	@GetMapping("/generateAccess")
//	public ResponseEntity<String> generateToken(@RequestParam String username){
//		return new ResponseEntity<String>(jwtService.createAccessToken(username),HttpStatus.OK) ;
//	}
//	
//	@GetMapping("/generateRefresh")
//	public ResponseEntity<String> generateExpireToken(@RequestParam String username){
//		return new ResponseEntity<String>(jwtService.createRefreshToken(username),HttpStatus.OK) ;
//	}
}
