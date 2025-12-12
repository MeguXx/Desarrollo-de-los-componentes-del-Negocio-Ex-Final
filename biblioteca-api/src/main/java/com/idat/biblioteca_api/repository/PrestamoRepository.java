package com.idat.biblioteca_api.repository;

import com.idat.biblioteca_api.entity.Prestamo;
import com.idat.biblioteca_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuario(Usuario usuario);
}

