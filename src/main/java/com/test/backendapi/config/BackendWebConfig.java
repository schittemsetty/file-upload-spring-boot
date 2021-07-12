package com.test.backendapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.test.backendapi.filter.JWTFilter;

@EnableWebSecurity
public class BackendWebConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JWTFilter jwtFilter;
	
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers(AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.POST, "/user/signIn", "/user/register").permitAll().anyRequest().authenticated();
		http.addFilter(jwtFilter);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
