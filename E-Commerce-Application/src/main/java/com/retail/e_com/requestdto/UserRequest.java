package com.retail.e_com.requestdto;

import com.retail.e_com.enums.UserRole;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	private String name;
	private String email;
	private String password;
	private UserRole userRole;
	
}
