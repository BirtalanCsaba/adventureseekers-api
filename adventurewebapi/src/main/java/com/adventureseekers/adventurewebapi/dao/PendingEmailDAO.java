package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adventureseekers.adventurewebapi.entity.PendingEmailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;

public interface PendingEmailDAO
	extends JpaRepository<PendingEmailEntity, UserEntity> {
	
	Optional<PendingEmailEntity> findByEmail(String email);
}
