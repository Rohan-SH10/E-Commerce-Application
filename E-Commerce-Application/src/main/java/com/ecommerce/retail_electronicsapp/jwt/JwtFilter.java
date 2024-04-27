package com.ecommerce.retail_electronicsapp.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.retail_electronicsapp.exceptions.AccountAceessRequestDeniedException;
import com.ecommerce.retail_electronicsapp.exceptions.TokenIsBlockedException;
import com.ecommerce.retail_electronicsapp.repository.AccessTokenRepository;
import com.ecommerce.retail_electronicsapp.repository.RefreshTokenRepository;
import com.ecommerce.retail_electronicsapp.repository.UserRepository;
import com.ecommerce.retail_electronicsapp.utility.TokenResponseStructure;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private AccessTokenRepository accessRepository;
	private RefreshTokenRepository refreshRepository;
	private JwtService jwtService;
	private UserRepository userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

//		String at = null;
//		String rt = null;
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				if (cookie.getName().equalsIgnoreCase("at"))
//					at = cookie.getValue();
//				if (cookie.getName().equalsIgnoreCase("rt"))
//					rt = cookie.getValue();
//
//			}
//		}
//		if (rt != null && at != null) {
//			if (accessRepository.existsByTokenAndIsBlocked(at, true)
//					&& refreshRepository.existsByTokenAndIsBlocked(rt, true)) {
//				throw new TokenIsBlockedException("Token is blocked");
//			}
//			
//			System.out.println("at and rt are valid");
//			String username = jwtService.getUsername(at);
//			String userRole = jwtService.getUserRole(at);
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
//					&& userRole != null) {
//				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
//						Collections.singleton(new SimpleGrantedAuthority(userRole)));
//				token.setDetails(new WebAuthenticationDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(token);
//			}
//		}

		Cookie[] cookies = request.getCookies();
		String accessToken=null;
		String refreshToken=null;
//		String refreshToken=Arrays.stream(request.getCookies())
//							.map(cookie->cookie.getAttribute("rt"))
//							.collect(Collectors.toList())
//							.get(0);

		if(cookies!=null) {
			for(Cookie cookie:cookies) {
				accessToken= cookie.getAttribute("at");
				refreshToken= cookie.getAttribute("rt");
			}
		}

		String userRole=null,username=null;
		if(accessToken!=null && refreshToken!=null) {
			try {
	            if(accessRepository.existsByTokenAndIsBlocked(accessToken,true)
	                    || refreshRepository.existsByTokenAndIsBlocked(refreshToken,true)) 
	                throw new AccountAceessRequestDeniedException("Access for this account is blocked");

	            else {
	                username=jwtService.getUsername(accessToken);
	                userRole=jwtService.getUserRole(accessToken);
	                if(username!=null && userRole!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
	                    UsernamePasswordAuthenticationToken token=
	                            new UsernamePasswordAuthenticationToken(username
	                                    , null
	                                    , Collections.singleton(new SimpleGrantedAuthority(userRole)));
	                    token.setDetails(new WebAuthenticationDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(token);
	                    System.out.println(token.getName());
	                }
	            }
	        } catch (ExpiredJwtException e) {
	            // Handling JWTExpired exception
	        	response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            Map<String, String> errors = new HashMap<>();
	            errors.put("error", "Unauthorized, access token has expired");
	            errors.put("message", e.getMessage());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//	            new ObjectMapper().writeValue(response.getOutputStream(),
//	            		TokenResponseStructure.failure(errors));
	            return;
	        } catch (JwtException e) {
	            // Handling JWT exception
	        	response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            Map<String, String> errors = new HashMap<>();
	            errors.put("error", "Unauthorized, malformed token or invalid signature");
	            errors.put("message", e.getMessage());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//	            new ObjectMapper().writeValue(response.getOutputStream(),
//	                    TokenResponseStructure.failure(errors));
	            return;
	        }
		}
		filterChain.doFilter(request, response);

	}

}
