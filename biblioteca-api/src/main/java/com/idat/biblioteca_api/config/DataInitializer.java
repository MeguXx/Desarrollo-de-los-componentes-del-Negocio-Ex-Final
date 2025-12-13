package com.idat.biblioteca_api.config;

import com.idat.biblioteca_api.entity.Rol;
import com.idat.biblioteca_api.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crea el rol ADMIN si no existe
        if (rolRepository.findByNombre("ADMIN").isEmpty()) {
            rolRepository.save(Rol.builder().nombre("ADMIN").build());
        }
        // Crea el rol USUARIO si no existe
        if (rolRepository.findByNombre("USUARIO").isEmpty()) {
            rolRepository.save(Rol.builder().nombre("USUARIO").build());
        }
    }
}