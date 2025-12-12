package com.idat.biblioteca_api.service;

import com.idat.biblioteca_api.entity.Prestamo;
import java.util.List;
import java.util.Optional;

public interface PrestamoService {
    List<Prestamo> listarTodos();
    Optional<Prestamo> buscarPorId(Long id);
    Prestamo guardar(Prestamo prestamo);
    void eliminar(Long id);
    List<Prestamo> listarPorUsuarioId(Long usuarioId);
    
    // AGREGA ESTE NUEVO MÉTODO:
    List<Prestamo> listarPorUsername(String username);
}