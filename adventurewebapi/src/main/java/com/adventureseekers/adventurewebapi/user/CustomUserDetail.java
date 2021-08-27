package com.adventureseekers.adventurewebapi.user;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.adventureseekers.adventurewebapi.entity.User;

public class CustomUserDetail implements UserDetails {

	private static final long serialVersionUID = 1L;
	private User user;
	Collection<? extends GrantedAuthority> authorities=null;
	
	public CustomUserDetail () {
		
	}
	
	public CustomUserDetail (User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnabled();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> collection) {
		this.authorities = collection;
	}
	
}
