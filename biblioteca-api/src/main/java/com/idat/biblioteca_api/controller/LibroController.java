package com.idat.biblioteca_api.controller;

import com.idat.biblioteca_api.entity.Libro;
import com.idat.biblioteca_api.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroRepository libroRepository;

    @GetMapping
    public List<Libro> listar() {
        return libroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtener(@PathVariable Long id) {
        return libroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        if (libro.getEjemplaresDisponibles() == null) {
            libro.setEjemplaresDisponibles(libro.getEjemplaresTotales());
        }
        return ResponseEntity.ok(libroRepository.save(libro));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroRepository.findById(id)
                .map(existing -> {
                    existing.setTitulo(libro.getTitulo());
                    existing.setAutor(libro.getAutor());
                    existing.setIsbn(libro.getIsbn());
                    existing.setAnioPublicacion(libro.getAnioPublicacion());
                    existing.setEjemplaresTotales(libro.getEjemplaresTotales());
                    existing.setEjemplaresDisponibles(libro.getEjemplaresDisponibles());
                    return ResponseEntity.ok(libroRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!libroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        libroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

