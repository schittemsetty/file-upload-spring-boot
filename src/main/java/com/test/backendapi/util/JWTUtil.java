package com.test.backendapi.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {
	
	@Value("${app.jwt.issuer}")
	private String issuer ;
	
	@Value("${app.jwt.secret}")
	private String secret;
	
	@Value("${app.jwt.expiry.time}")
	private int expiryTime;
	
	public String generateToken(String userName) {
		Algorithm algorithm = Algorithm.HMAC256(this.secret);
		long time = System.currentTimeMillis();
		Date issuedAt  = new Date(time);
		Date expiresAt = new Date(time + this.expiryTime);
		
		String token = JWT.create()
				.withSubject(userName)
				.withIssuedAt(issuedAt)
				.withExpiresAt(expiresAt)
				.withIssuer(this.issuer)
				.sign(algorithm);
		return token ;
	}
	
	public String verifyToken(String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(this.secret);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(this.issuer)
		        .build();
		    DecodedJWT jwt = verifier.verify(token);
		    return jwt.getSubject();
		} catch (JWTVerificationException e){
		    e.printStackTrace();
		}
		return null ;
	}

}
