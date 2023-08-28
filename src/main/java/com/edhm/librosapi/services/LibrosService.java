package com.edhm.librosapi.services;

import java.util.List;

import com.edhm.librosapi.entities.Libro;

public interface LibrosService {
	public List<Libro> findAll();

	public Libro findById(Long id);

	public void save(Libro libro);

	public void deleteById(Long id);
}
