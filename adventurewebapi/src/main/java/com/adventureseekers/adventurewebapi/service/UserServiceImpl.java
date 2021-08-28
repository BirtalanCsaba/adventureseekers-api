package com.adventureseekers.adventurewebapi.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.RoleDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.Role;
import com.adventureseekers.adventurewebapi.entity.User;
import com.adventureseekers.adventurewebapi.exception.UserAlreadyExistException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public Optional<User> findByUserName(String userName) {
		// check the database if the user already exists
		return this.userDAO.findByUsername(userName);
	}
	
	@Override
	@Transactional
	public void save(User newUser) {
		// check the uniqueness of the user 
		if (this.userDAO.existsByUsername(newUser.getUserName())) {
			throw new UserAlreadyExistException("A user already exists with username - " + newUser.getUserName());
		}
		if (this.userDAO.existsByEmail(newUser.getEmail())) {
			throw new UserAlreadyExistException("A user already exists with email - " + newUser.getEmail());
		}
		
		User user = new User();
		
		// assign user details to the user object
		user.setUserName(newUser.getUserName());
		user.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		user.setEmail(newUser.getEmail());
		user.setBirthDate(newUser.getBirthDate());
		
		// give user default role of "employee"
		user.setRoles(Arrays.asList(this.roleDAO.findRoleByName("ROLE_STANDARD").get()));
		
		// save the user in the database
		this.userDAO.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = 
				this.userDAO.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

		// check whether the user account is confirmed
		if (!user.isEnabled()) {
			throw new UsernameNotFoundException("The user account is not enabled");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
		/*CustomUserDetail customUserDetail = new CustomUserDetail();
		customUserDetail.setUser(user);
		customUserDetail.setAuthorities(mapRolesToAuthorities(user.getRoles()));
		
		return customUserDetail;*/
	}
	
	/**
	 * Converts the roles to a map of authorities
	 * @param collection The roles to be converted
	 * @return A map of authorities
	 */
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> collection) {
		return collection.stream().map(role -> 
						new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}	
}














