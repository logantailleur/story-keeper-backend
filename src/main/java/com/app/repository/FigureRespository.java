package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.model.Figure;
import com.app.model.World;

@Repository
public interface FigureRespository extends BaseRepository<Figure, Long> {
	List<Figure> findByWorld(World world);

	Optional<Figure> findByIdAndWorld(Long id, World world);

	List<Figure> findByWorldOrderByCreatedAtAsc(World world);

	Page<Figure> findByWorldOrderByCreatedAtAsc(World world, Pageable pageable);

	@Query("SELECT f FROM Figure f WHERE f.world = :world AND " +
			"(LOWER(COALESCE(f.name, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(COALESCE(f.description, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
			"ORDER BY f.createdAt ASC")
	List<Figure> findByWorldAndNameOrDescriptionContaining(@Param("world") World world, @Param("search") String search);

	@Query("SELECT f FROM Figure f WHERE f.world = :world AND " +
			"(LOWER(COALESCE(f.name, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(COALESCE(f.description, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
			"ORDER BY f.createdAt ASC")
	Page<Figure> findByWorldAndNameOrDescriptionContaining(@Param("world") World world, @Param("search") String search,
			Pageable pageable);
}
