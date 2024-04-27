package com.ecommerce.retail_electronicsapp.responsedto;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.ecommerce.retail_electronicsapp.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthResponse {

	private int userId;
	private String username;
	
	private String displayName;
	private LocalDateTime accessExpiration;
	
	private LocalDateTime refreshExpiration;
	private UserRole userRole;
	private boolean authenticated;
	
	
//	public AuthResponse(@Value("${myapp.jwt.access.expiration}") long access,@Value("${myapp.jwt.refresh.expiration}") long refresh) {
//		this.accessExpiration=access/1000;
//		this.refreshExpiration=refresh/1000;
//	}
}
