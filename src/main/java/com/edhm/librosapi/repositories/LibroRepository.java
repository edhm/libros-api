package com.edhm.librosapi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.edhm.librosapi.entities.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {
	@Override
	List<Libro> findAll();
}
