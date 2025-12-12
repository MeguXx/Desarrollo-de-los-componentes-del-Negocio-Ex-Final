package com.idat.biblioteca_api.repository;

import com.idat.biblioteca_api.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}

