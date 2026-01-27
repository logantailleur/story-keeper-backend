package com.app.repository;

import org.springframework.stereotype.Repository;

import com.app.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
	User findByEmail(String email);

	Boolean existsByEmail(String email);
}
