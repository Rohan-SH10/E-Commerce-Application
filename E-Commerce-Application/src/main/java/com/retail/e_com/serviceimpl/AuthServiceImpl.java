package com.retail.e_com.serviceimpl;

import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.exception.OtpExpiredException;
import com.retail.e_com.exception.RegistrationSessionExpiredException;
import com.retail.e_com.mailservice.MailService;
import com.retail.e_com.requestdto.OtpRequest;
import com.retail.e_com.mailservice.MessageModel;
import com.retail.e_com.util.SimpleResponseStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.InvalidUserRoleException;
import com.retail.e_com.exception.UserExistedByEmailException;
import com.retail.e_com.repository.UserRepo;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.util.ResponseStructure;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

	private UserRepo userRepo;
	private ResponseStructure<UserResponse> responseStructure;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private SimpleResponseStructure simpleResponseStructure;
	private MailService mailService;
	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) {
		
		if(userRepo.existsByEmail(userRequest.getEmail()))
		   throw new UserExistedByEmailException("Invalid user");
		User user = mapToChildEntity(userRequest);
		String otp = generateOtp();
//		if(otpCache.getCache(user.getEmail())!=null)
//			throw new RuntimeException("Already Otp Exists to this user");
		otpCache.add(user.getEmail(),otp);
		userCache.add(user.getEmail(),user);
		//Send  mail with otp
		try {
			sendOtp(user, otp);
		}
		catch (MessagingException e ){
			e.getMessage();
		}
		System.out.println(otp);


		return ResponseEntity.ok(simpleResponseStructure.setMessage("Verify OTP Sent through mail to complete the registration " +
						"! OTP expires in 1 minute")
				.setStatusCode(HttpStatus.ACCEPTED.value()));

	}

	private void sendOtp(User user, String otp) throws MessagingException {
		MessageModel model=MessageModel.builder().to(user.getEmail()).subject("Verify Your Otp").text("<p>Hi, <br>" +
				"Thanks for your interest in E-Com , please Verify your mail Id using the " +
				"OTP given below </p>" +
				"<br>" +
				"<h1>"+otp+"</h1>" +
				"<br>" +
				"<p>Please ignore if it's not you</p>" +
				"<br>" +
				"<p>with best regards</p>" +
				"<h3>E-Com</h3>" +
				"<br>" +
				"<img height=200px width=200px src=https://w7.pngwing.com/pngs/160/304/png-transparent-flipkart-logo.png>").build();
		mailService.sendMailMessage(model);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OtpRequest otpRequest) {
		if(otpCache.getCache(otpRequest.getEmail())==null) throw new OtpExpiredException("Invalid OTP");
		if(!otpCache.getCache(otpRequest.getEmail()).equals((otpRequest.getOtp())))
			throw new RuntimeException("invalid otp");
		User user = userCache.getCache(otpRequest.getEmail());
		if(user==null) throw new RegistrationSessionExpiredException("Invalid OTP");
		user.setIsEmailVerified(true);
		//User user = userRepo.save(u);
		return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
				.setMessage("User Email verified successfully")
				.setData(mapToUserResponse(user)));
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