package com.app.repository;

import org.springframework.stereotype.Repository;

import com.app.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
	// Additional custom query methods can be added here if needed
}
