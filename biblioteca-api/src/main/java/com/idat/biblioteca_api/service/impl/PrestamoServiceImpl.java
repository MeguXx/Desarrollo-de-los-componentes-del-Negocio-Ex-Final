package com.idat.biblioteca_api.service.impl;

import com.idat.biblioteca_api.entity.Prestamo;
import com.idat.biblioteca_api.entity.Usuario;
import com.idat.biblioteca_api.repository.PrestamoRepository;
import com.idat.biblioteca_api.repository.UsuarioRepository; // IMPORTAR ESTO
import com.idat.biblioteca_api.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired // INYECTAMOS ESTO PARA BUSCAR AL USUARIO
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Optional<Prestamo> buscarPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public void eliminar(Long id) {
        prestamoRepository.deleteById(id);
    }

    @Override
    public List<Prestamo> listarPorUsuarioId(Long usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return prestamoRepository.findByUsuario(usuario);
    }

    // IMPLEMENTACIÓN DEL NUEVO MÉTODO
    @Override
    public List<Prestamo> listarPorUsername(String username) {
        // 1. Buscamos al usuario por su nombre (el que viene en el Token)
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 2. Buscamos sus préstamos
        return prestamoRepository.findByUsuario(usuario);
    }
}