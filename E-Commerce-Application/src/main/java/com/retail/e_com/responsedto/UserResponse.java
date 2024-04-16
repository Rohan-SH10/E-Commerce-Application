package com.retail.e_com.responsedto;

import com.retail.e_com.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

	private int userId;
	private String username;
	private String displayName;
	private String email;
	private boolean isEmailVerified;
	private boolean isDeleted;
	private UserRole userRole;
	
}
