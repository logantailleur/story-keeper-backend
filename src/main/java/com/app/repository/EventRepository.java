package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.app.model.Event;
import com.app.model.World;

@Repository
public interface EventRepository extends BaseRepository<Event, Long> {
	List<Event> findByWorld(World world);

	Optional<Event> findByIdAndWorld(Long id, World world);

	List<Event> findByWorldOrderByYearAsc(World world);
}
