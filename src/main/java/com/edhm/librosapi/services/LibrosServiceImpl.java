package com.edhm.librosapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edhm.librosapi.entities.Libro;
import com.edhm.librosapi.repositories.LibroRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LibrosServiceImpl implements LibrosService {

	@Autowired
	private LibroRepository libroRepository;

	@Override
	public List<Libro> findAll() {
		return libroRepository.findAll();
	}

	@Override
	public Libro findById(Long id) {
		return libroRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No existe registro"));
	}

	@Override
	public void save(Libro libro) {
		libroRepository.save(libro);
	}

	@Override
	public void deleteById(Long id) {
		libroRepository.deleteById(id);
	}

}
