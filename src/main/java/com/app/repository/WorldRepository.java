package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.app.model.User;
import com.app.model.World;

@Repository
public interface WorldRepository extends BaseRepository<World, Long> {
	List<World> findByUser(User user);

	Optional<World> findByIdAndUser(Long id, User user);
}
