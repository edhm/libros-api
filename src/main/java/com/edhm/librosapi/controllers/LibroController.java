package com.edhm.librosapi.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edhm.librosapi.entities.Libro;
import com.edhm.librosapi.services.LibrosService;

@RestController
public class LibroController {
	private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

	@Value("${app.storage.path}")
	private String STORAGEPATH;

	@Autowired
	private LibrosService librosService;

	@GetMapping("/libros")
	public List<Libro> libros() {
		logger.info("call libros");
		List<Libro> libros = librosService.findAll();
		logger.info("libros: " + libros);
		return libros;
	}

	@GetMapping("/libros/images/{filename:.+}")
	public ResponseEntity<Resource> files(@PathVariable String filename) throws Exception {
		logger.info("call images: " + filename);
		Path path = Paths.get(STORAGEPATH).resolve(filename);
		logger.info("Path: " + path);
		if (!Files.exists(path)) {
			return ResponseEntity.notFound().build();
		}
		Resource resource = new UrlResource(path.toUri());
		logger.info("Resource: " + resource);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + resource.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(STORAGEPATH).resolve(filename)))
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength())).body(resource);
	}

	@PostMapping("/libros")
	public Libro crear(@RequestParam(name = "imagen", required = false) MultipartFile imagen,
			@RequestParam("titulo") String titulo, @RequestParam("autor") String autor,
			@RequestParam("anioDePubblicacion") int anioDePubblicacion, @RequestParam("isbn") String isbn,
			@RequestParam("costo") int costo, @RequestParam("numPaginas") int numPaginas) throws Exception {
		logger.info("call crear(" + titulo + ", " + autor + ", " + anioDePubblicacion + ", " + isbn + ", " + costo
				+ ", " + numPaginas + ")");
		Libro libro = new Libro();
		libro.setTitulo(titulo);
		libro.setAutor(autor);
		libro.setanioDePubblicacion(anioDePubblicacion);
		libro.setIsbn(isbn);
		libro.setCosto(costo);
		libro.setNumPaginas(numPaginas);

		if (imagen != null && !imagen.isEmpty()) {
			String filename = System.currentTimeMillis()
					+ imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf("."));
			libro.setImagen(filename);
			if (Files.notExists(Paths.get(STORAGEPATH))) {
				Files.createDirectories(Paths.get(STORAGEPATH));
			}
			Files.copy(imagen.getInputStream(), Paths.get(STORAGEPATH).resolve(filename));
		}
		librosService.save(libro);
		return libro;

	}
}
