package com.ecommerce.retail_electronicsapp.service.impl;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.retail_electronicsapp.cache.CacheStore;
import com.ecommerce.retail_electronicsapp.entity.AccessToken;
import com.ecommerce.retail_electronicsapp.entity.Customer;
import com.ecommerce.retail_electronicsapp.entity.RefreshToken;
import com.ecommerce.retail_electronicsapp.entity.Seller;
import com.ecommerce.retail_electronicsapp.entity.User;
import com.ecommerce.retail_electronicsapp.enums.UserRole;
import com.ecommerce.retail_electronicsapp.exceptions.EmailAlreadyExistsException;
import com.ecommerce.retail_electronicsapp.exceptions.IllegalAccessRequestExcpetion;
import com.ecommerce.retail_electronicsapp.exceptions.InvalidLoginRequestException;
import com.ecommerce.retail_electronicsapp.exceptions.JwtTokensMissingException;
import com.ecommerce.retail_electronicsapp.exceptions.OTPExpiredException;
import com.ecommerce.retail_electronicsapp.exceptions.OTPInvalidException;
import com.ecommerce.retail_electronicsapp.exceptions.RegistrationSessionExpiredException;
import com.ecommerce.retail_electronicsapp.jwt.JwtService;
import com.ecommerce.retail_electronicsapp.mailservice.MailService;
import com.ecommerce.retail_electronicsapp.mailservice.MessageModel;
import com.ecommerce.retail_electronicsapp.repository.AccessTokenRepository;
import com.ecommerce.retail_electronicsapp.repository.RefreshTokenRepository;
import com.ecommerce.retail_electronicsapp.repository.UserRepository;
import com.ecommerce.retail_electronicsapp.requestdto.AuthRequest;
import com.ecommerce.retail_electronicsapp.requestdto.OTPRequest;
import com.ecommerce.retail_electronicsapp.requestdto.UserRequest;
import com.ecommerce.retail_electronicsapp.responsedto.AuthResponse;
import com.ecommerce.retail_electronicsapp.responsedto.UserResponse;
import com.ecommerce.retail_electronicsapp.service.AuthService;
import com.ecommerce.retail_electronicsapp.utility.ResponseStructure;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;

@Service

public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;
	//	private CustomerRepository customerRepo;
	//	private SellerRepository sellerRepo;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private ResponseStructure<UserResponse> userRespStruct;
	private ResponseStructure<AuthResponse> authResponseStructure;
	private SimpleResponseStructure simpleResponse;
	private MailService mailService;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private AccessTokenRepository accessTokenRepository;
	private RefreshTokenRepository refreshTokenRepository;
	private PasswordEncoder passwordEncoder;




	public AuthServiceImpl(UserRepository userRepository, CacheStore<String> otpCache, CacheStore<User> userCache,
			ResponseStructure<UserResponse> userRespStruct, ResponseStructure<AuthResponse> authResponseStructure,
			SimpleResponseStructure simpleResponse, MailService mailService,
			AuthenticationManager authenticationManager, JwtService jwtService,
			AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.userRespStruct = userRespStruct;
		this.authResponseStructure = authResponseStructure;
		this.simpleResponse = simpleResponse;
		this.mailService = mailService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.accessTokenRepository = accessTokenRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;
	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiration;


	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) throws MessagingException {
		if(userRepository.existsByEmail(userRequest.getEmail())) throw new EmailAlreadyExistsException("Email Already exists! please re-verify your account");
		User user=mapToUsersChildEntity(userRequest);
		String otp=generateOTP();
		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);
		System.out.println(otp);

		//should send otp to mail
		sendOTP(user,otp);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(simpleResponse
						.setMessage("verify otp sent through mail to complete registration,OTP expires in 5 minutes")
						.setStatus(HttpStatus.ACCEPTED.value()));
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest) {
		String email=otpRequest.getEmail(); //can't be null after validations

		if(otpCache.get(email)==null) throw new OTPExpiredException("OTP Has expired, please register again");
		if(!otpCache.get(email).equals(otpRequest.getOtp())) throw new OTPInvalidException("please enter valid OTP");

		User user=userCache.get(email);// will be either Customer (or) Seller
		if(user==null) throw new RegistrationSessionExpiredException("User , please register again");
		user.setEmailVerified(true);
		user=userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userRespStruct.setStatusCode(HttpStatus.CREATED.value())
				.setMessage("OTP verified succesfully")
				.setData(mapToUserResponse(user)));
	}



	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest,String accessToken,String refreshToken) {
		if(accessToken==null && refreshToken!=null) throw new InvalidLoginRequestException("user already logged in, send a refresh request");
		String username = authRequest.getUsername().split("@gmail.com")[0];

		System.err.println(username);
		System.err.println(authRequest.getPassword());

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println(authentication.isAuthenticated());

			if(! authentication.isAuthenticated())throw new RuntimeException();
			SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			HttpHeaders httpHeaders = new HttpHeaders();

			return userRepository.findByUsername(username).map(user -> {
				generateAccessToken(user, httpHeaders);
				generateRefreshToken(user, httpHeaders);

				return ResponseEntity.ok()
						.headers(httpHeaders)
						.body(authResponseStructure.setStatusCode(HttpStatus.OK.value())
								.setMessage("Logged in")
								.setData(mapToAuthResponse(user)));
			}).orElse(ResponseEntity.notFound().build());
		} catch (AuthenticationException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(authResponseStructure.setStatusCode(HttpStatus.UNAUTHORIZED.value())
							.setMessage("Authentication failed: " + ex.getMessage()));
		}
	}


	@Override
	public ResponseEntity<SimpleResponseStructure> userLogout(String accessToken, String refreshToken) {

		if(accessToken==null && refreshToken==null) throw new JwtTokensMissingException("Token Not Found");

		AccessToken aToken = accessTokenRepository.findByToken(accessToken).get();
		RefreshToken rToken = refreshTokenRepository.findByToken(refreshToken).get();

		aToken.setIsBlocked(true);
		accessTokenRepository.save(aToken);

		rToken.setIsBlocked(true);
		refreshTokenRepository.save(rToken);

		HttpHeaders headers = new HttpHeaders();

		invalidateCookies(headers,aToken,rToken);

		return ResponseEntity.ok().headers(headers).body(simpleResponse.setStatus(HttpStatus.OK.value()).setMessage("Tokens are set Blocked"));
	}



	
	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshAccessTokens(String accessToken, String refreshToken) {
		HttpHeaders headers=new HttpHeaders();
		
		//if(accessToken==null && refreshToken==null) throw new TokenExpiredException("User Logged Out");
		if(accessTokenRepository.existsByToken(accessToken)) {
			AccessToken at = accessTokenRepository.findByToken(accessToken).get();
			at.setIsBlocked(true);
			accessTokenRepository.save(at);
		}
		if(!refreshTokenRepository.existsByToken(refreshToken))throw new JwtTokensMissingException("User is Logged out");
		RefreshToken rt = refreshTokenRepository.findByToken(refreshToken).get();
		
		if(jwtService.getIssuedAt(refreshToken).before(new Date())) {
			generateRefreshToken(rt.getUser(), headers);
			
		}
		
		else {
			headers.add(HttpHeaders.SET_COOKIE, rt.getToken());
			
		}
			generateAccessToken(rt.getUser(), headers);
			
		
		return ResponseEntity.ok().headers(headers).body(authResponseStructure
				.setData(mapToAuthResponse(rt.getUser()))
				.setMessage("Refresh Request sent successfully")
				.setStatusCode(HttpStatus.OK.value()));
	}


	
	


	private void invalidateCookies(HttpHeaders headers, AccessToken aToken, RefreshToken rToken) {
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("at", "",0));
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", "",0));
		
	}
	


	private void sendOTP(User user, String otp) throws MessagingException {
		MessageModel model = MessageModel.builder().to(user.getEmail())
				.subject("Email Verification")
				.message(
						"<p>Hi, <br>"
								+ "Thank you for showing interest to shop in Flipkart,"
								+ "<br>"
								+ " please Verify your Email ID : "+user.getEmail()+" using the OTP given below.</p>"
								+ "<br>"
								+ "<h1> OTP : "+otp+"</h1>"
								+ "<br><br>"
								+ "<p>Please Ignore if you haven't requested for verification</p>"
								+ "<br>"
								+ "<p>With best regards,</p>"
								+ "<h3>Flipkart</h3>"
								+ "<img src="+"'https://deep-image.ai/blog/content/images/2023/01/Flipkart-logo.png'"+" alt='Flipkart logo'/>"
						)
				.build();
		mailService.sendMessage(model);
	}




	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000,999999));
	}

	private UserResponse mapToUserResponse(User user) {
		UserResponse response = UserResponse.builder()
				.displayName(user.getDisplayName()).userId(user.getUserId())
				.username(user.getUsername()).email(user.getEmail())
				.userRole(user.getUserRole()).isDeleted(user.isDeleted())
				.isEmailVerified(user.isEmailVerified())
				.build();
		return response;
	}

	private User mapToUsersChildEntity(UserRequest userRequest) {
		UserRole role=userRequest.getUserRole();
		User user=null;

		switch(role) {
		case CUSTOMER -> user=new Customer();
		case SELLER -> user=new Seller();
		default -> throw new IllegalAccessRequestExcpetion("Invalid Role");
		}

		user.setDisplayName(userRequest.getName())
		.setDeleted(false).setEmailVerified(false)
		.setEmail(userRequest.getEmail()).setPassword(passwordEncoder.encode(userRequest.getPassword()))
		.setUserRole(role).setUsername(userRequest.getEmail().split("@gmail.com")[0]);
		return user;
	}






	private AuthResponse mapToAuthResponse(User user) {

		return AuthResponse.builder().userId(user.getUserId())
				.username(user.getUsername())
				.userRole(user.getUserRole())
				.displayName(user.getDisplayName())
				.authenticated(user.isEmailVerified())
				.accessExpiration(accessExpiration)
				.refreshExpiration(refreshExpiration)
				.build();



	}

	private void generateAccessToken(User user,HttpHeaders headers) {

		String token = jwtService.createAccessToken(user.getUsername(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("at",token,accessExpiration));
		AccessToken accessToken = new AccessToken();
		accessToken.setToken(token);
		accessToken.setUser(user);
		accessToken.setIsBlocked(false);
		accessToken.setExpiration(new Date(new Date().getTime()+(accessExpiration)));
		accessTokenRepository.save(accessToken);
	}


	private String configureCookie(String name, String value, long maxAge) {
		return ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.maxAge(Duration.ofMillis(maxAge))

				.httpOnly(true)
				.secure(false) 
				.sameSite("Lax")
				.build().toString();
	}

	private void generateRefreshToken(User user,HttpHeaders headers) {
		String token = jwtService.createRefreshToken(user.getUsername(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt",token,refreshExpiration));
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(token);
		refreshToken.setUser(user);
		refreshToken.setIsBlocked(false);
		refreshToken.setExpiration(new Date(new Date().getTime()+(refreshExpiration)));
		refreshTokenRepository.save(refreshToken);

	}

}