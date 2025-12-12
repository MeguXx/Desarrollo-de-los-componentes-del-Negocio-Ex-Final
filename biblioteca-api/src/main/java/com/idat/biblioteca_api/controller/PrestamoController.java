package com.idat.biblioteca_api.controller;

import com.idat.biblioteca_api.entity.Prestamo;
import com.idat.biblioteca_api.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder; // IMPORTAR
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    // --- NUEVO ENDPOINT: MIS PRÉSTAMOS (Ponerlo ARRIBA del /{id}) ---
    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<Prestamo>> misPrestamos() {
        // Obtenemos el usuario que inició sesión desde el Token
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        List<Prestamo> lista = prestamoService.listarPorUsername(username);
        
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
    // ---------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<Prestamo>> listarTodos() {
        return ResponseEntity.ok(prestamoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        Optional<Prestamo> prestamo = prestamoService.buscarPorId(id);
        return prestamo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prestamo> guardar(@RequestBody Prestamo prestamo) {
        if (prestamo.getFechaPrestamo() == null) prestamo.setFechaPrestamo(LocalDate.now());
        if (prestamo.getDevuelto() == null) prestamo.setDevuelto(false);

        Prestamo nuevoPrestamo = prestamoService.guardar(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable Long id, @RequestBody Prestamo prestamoRequest) {
        Optional<Prestamo> prestamoOptional = prestamoService.buscarPorId(id);
        if (prestamoOptional.isPresent()) {
            Prestamo prestamoExistente = prestamoOptional.get();
            prestamoExistente.setDevuelto(prestamoRequest.getDevuelto());
            prestamoExistente.setFechaDevolucion(prestamoRequest.getFechaDevolucion());
            return ResponseEntity.ok(prestamoService.guardar(prestamoExistente));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Prestamo> prestamo = prestamoService.buscarPorId(id);
        if (prestamo.isPresent()) {
            prestamoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Prestamo> prestamos = prestamoService.listarPorUsuarioId(usuarioId);
        if (prestamos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(prestamos);
    }
}