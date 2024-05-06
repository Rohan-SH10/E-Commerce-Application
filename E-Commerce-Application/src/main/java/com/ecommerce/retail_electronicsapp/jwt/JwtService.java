package com.ecommerce.retail_electronicsapp.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.retail_electronicsapp.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
public class JwtService  {

	
	@Value("${myapp.jwt.secret}")
	private String secret;
	
	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiry;
	
	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiry;
	
	public String createAccessToken(String username,String role) {
		return genereateToken(accessExpiry,role, username);
		
	}
	public String createRefreshToken(String username,String role) {
		return genereateToken(refreshExpiry,role, username);
	}
	
	public String getUsername(String jwtToken) {
		return parseJwtClaims(jwtToken).getSubject();
	}
	
	public String getUserRole(String token) {
		return parseJwtClaims(token).get("role", String.class);
	}
	
	public Date getIssuedAt(String token) {
		return parseJwtClaims(token).getIssuedAt();
	}
	public Date getExpiration(String token) {
		return parseJwtClaims(token).getExpiration();
	}
	private String genereateToken(long expiration,String role, String username) {
//		Map<String,Object> uniqueMap = new 
//		uniqueMap.put(username+" "+new Random().nextInt(100000,999999),username );
		
		return Jwts.builder().setClaims(Maps.of("role", role).build()).setSubject(username)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+expiration))
		//.addClaims(new HashMap<>())->behaves same as set claims initialised at first
		//.claim(K,V)-> takes directly the key value pair
		.signWith(getSignatureKey(),SignatureAlgorithm.HS256)
		.compact();
	}
	
	private Key getSignatureKey() {
//		byte[] decode = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
	
	
	
	private Claims parseJwtClaims(String jwtToken) {
		return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(jwtToken).getBody();
	}
	
	
	
}