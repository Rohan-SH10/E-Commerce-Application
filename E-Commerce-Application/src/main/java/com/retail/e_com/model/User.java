package com.retail.e_com.model;

import com.retail.e_com.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(name = "username")
	@NotNull
	private String username;
	private String displayName;
	
	@NotBlank(message = "Invalid Email")
	@NotNull(message="Invlaid Email")
	@Column(unique = true , name = "useremail")
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",message = "Invlaid Email format")
	@Schema(required = true)
	private String email;
	
	@NotBlank(message = "Invalid Password")
	@NotNull(message="Invlaid Password")
	@Size(min=8,max=20,message = "Password must be beyween 8 and 20 charecter")
	@Pattern(regexp = "^(?=.[A-Za-z])(?=.\\d)(?=.[@#$%^&+=!])(?!.\\s).{8,}$",
	message = "password must contain at least one letter,one number,one special charecter")
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	private Boolean isEmailVerified;
	private Boolean isDeleted;
}