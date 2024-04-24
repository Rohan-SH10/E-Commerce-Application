package com.ecommerce.retail_electronicsapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.retail_electronicsapp.jwt.JwtFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
	
	private RetailUserDetailService userDetailService;
	private JwtFilter jwtFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf->csrf.disable())
				//only one HttpMethod and String[] is allowed for this overloaded method
//				.authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.GET,"/users/{userId}").hasAuthority("read"))
//				.authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.PUT,"/users/{userId}").hasAuthority("write"))
//				.authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.POST,"/users/register").hasAuthority("write"))
				.authorizeHttpRequests(auth->auth.requestMatchers("/**")
						.permitAll()
						.anyRequest()
						.authenticated())
				.sessionManagement(management-> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);	//hashes password 12 times which is widely used
	}

	//bean methods must always be default
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailService);
		return authenticationProvider;
	}
}
