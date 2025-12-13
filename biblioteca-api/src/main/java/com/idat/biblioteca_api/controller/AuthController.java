package com.idat.biblioteca_api.controller;

import com.idat.biblioteca_api.dto.AuthResponse;
import com.idat.biblioteca_api.dto.LoginRequest;
import com.idat.biblioteca_api.dto.RegisterRequest;
import com.idat.biblioteca_api.entity.Rol;
import com.idat.biblioteca_api.entity.Usuario;
import com.idat.biblioteca_api.repository.RolRepository;
import com.idat.biblioteca_api.repository.UsuarioRepository;
import com.idat.biblioteca_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // --- MÉTODO REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("El username ya existe");
        }

        Set<Rol> rolesAsignados = new HashSet<>();

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            // Si no manda nada, le asignamos USUARIO por defecto
            Rol rolUsuario = rolRepository.findByNombre("USUARIO")
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre("USUARIO").build()));
            rolesAsignados.add(rolUsuario);
        } else {
            request.getRoles().forEach(rolNombre -> {
                String nombreRol = rolNombre.toUpperCase();
                Rol rol = rolRepository.findByNombre(nombreRol)
                        .orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombreRol).build()));
                rolesAsignados.add(rol);
            });
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombreCompleto(request.getNombreCompleto())
                .roles(rolesAsignados)
                .build();

        usuarioRepository.save(usuario);

        var userDetails = usuarioRepository.findByUsername(request.getUsername()).get();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // --- MÉTODO LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        
        // 1. Autenticar con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Buscar usuario
        var userDetails = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // 3. Generar token
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}