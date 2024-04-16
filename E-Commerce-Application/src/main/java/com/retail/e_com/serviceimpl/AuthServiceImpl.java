package com.retail.e_com.serviceimpl;

import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.util.SimpleResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.InvalidUserRoleException;
import com.retail.e_com.exception.UserExistedByEmailException;
import com.retail.e_com.repository.CustomerRepo;
import com.retail.e_com.repository.SellerRepo;
import com.retail.e_com.repository.UserRepo;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.util.ResponseStructure;

import lombok.AllArgsConstructor;

import java.util.Random;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

	private UserRepo userRepo;
	private ResponseStructure<UserResponse> responseStructure;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private SimpleResponseStructure simpleResponseStructure;

	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) {
		
		if(userRepo.existsByEmail(userRequest.getEmail()))
		   throw new UserExistedByEmailException("Invalid user");
		User user = mapToChildEntity(userRequest);
		String otp = generateOtp();
		otpCache.add("otp",otp);
		userCache.add("user",user);


		return ResponseEntity.ok(simpleResponseStructure.setMessage("Verify OTP Sent through mail to complete the registration " +
						"! OTP expires in 1 minute")
				.setStatusCode(HttpStatus.OK.value()));

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(String otp) {
		if(!otpCache.getCache("otp").equals((otp)))
			throw new RuntimeException("invalid otp");
		User user = userCache.getCache("user");
		user.setIsEmailVerified(true);
		//User user = userRepo.save(u);
		return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("User Email verified successfully").setData(mapToUserResponse(user)));
	}

	private String generateOtp() {
		return String.valueOf(new Random().nextInt(100000,999999));
	}

	private <T extends  User> T mapToChildEntity(UserRequest userRequest) {
		UserRole role = userRequest.getUserRole();
		System.out.println(role);
		User user = null;
		switch (role){
			case SELLER -> user = new Seller();
			case CUSTOMER -> user = new Customer();
			default -> throw new InvalidUserRoleException("Invalid user role");
		}

		user.setDisplayName(userRequest.getName());
		user.setUsername(userRequest.getEmail().split("@gmail.com")[0]);
		user.setEmail(userRequest.getEmail());
		user.setPassword(userRequest.getPassword());
		user.setIsEmailVerified(false);
		user.setIsDeleted(false);
		user.setUserRole(userRequest.getUserRole());

		return (T) user;

	}

	private UserResponse mapToUserResponse(User user) {
		
		return UserResponse.builder().displayName(user.getDisplayName())
				.userId(user.getUserId()).username(user.getUsername())
				.email(user.getEmail())
				.isDeleted(user.getIsDeleted())
				.isEmailVerified(user.getIsEmailVerified()).userRole(user.getUserRole()).build();
	}


	
}