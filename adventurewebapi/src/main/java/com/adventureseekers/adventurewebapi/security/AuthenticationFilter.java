package com.adventureseekers.adventurewebapi.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.adventureseekers.adventurewebapi.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.adventureseekers.adventurewebapi.config.SecurityConstants;

import java.security.Key;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
				HttpServletResponse res) throws AuthenticationException {
		try {
			User applicationUser = new ObjectMapper().readValue(req.getInputStream(), User.class);
    	
			return authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(applicationUser.getUserName(),
    					applicationUser.getPassword(), new ArrayList<>()));
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                        Authentication auth) throws IOException, ServletException {

		Date exp = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
		Key key = this.getSigningKey();
		Claims claims = Jwts.claims().setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername());
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).setExpiration(exp).compact();
		res.addHeader("token", token);
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = SecurityConstants.KEY.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
