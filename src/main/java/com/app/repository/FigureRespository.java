package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.app.model.Figure;
import com.app.model.World;

@Repository
public interface FigureRespository extends BaseRepository<Figure, Long> {
	List<Figure> findByWorld(World world);

	Optional<Figure> findByIdAndWorld(Long id, World world);

	List<Figure> findByWorldOrderByCreatedAtAsc(World world);
}
