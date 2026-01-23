package com.storykeeper.repository;

import org.springframework.stereotype.Repository;

import com.storykeeper.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
	// Additional custom query methods can be added here if needed
}
