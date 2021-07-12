package com.test.backendapi.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.test.backendapi.util.JWTUtil;

@Component
public class JWTFilter extends BasicAuthenticationFilter {
	
	Logger logger = LoggerFactory.getLogger(JWTFilter.class);
	
	@Autowired
	JWTUtil JWTUtil;

	public static final String TOKEN_PREFIX = "Bearer ";
	
	public JWTFilter() {
		super(new NoOpAuthenticationManager());
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Validates JWT
		String token = request.getHeader("Authorization");
		if ( token != null && token.startsWith(TOKEN_PREFIX)) {
			String user = JWTUtil.verifyToken(token.replace(TOKEN_PREFIX, ""));
			if ( user != null ) {
				UsernamePasswordAuthenticationToken userPwdtkn = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
				SecurityContextHolder.getContext().setAuthentication(userPwdtkn);
			}
		}
		chain.doFilter(request, response);
	}

}
