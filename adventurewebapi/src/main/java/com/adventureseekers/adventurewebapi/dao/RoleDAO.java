package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adventureseekers.adventurewebapi.entity.Role;
import com.adventureseekers.adventurewebapi.entity.User;

public interface RoleDAO
		extends JpaRepository<User, Long>{
	
	/**
	 * Gets the role by name
	 * @param theRoleName The name of the role
	 * @return The role object with the given name
	 */
	@Query("SELECT r FROM Role r WHERE r.name =:rName")
	public Optional<Role> findRoleByName(@Param("rName")String theRoleName);
}
