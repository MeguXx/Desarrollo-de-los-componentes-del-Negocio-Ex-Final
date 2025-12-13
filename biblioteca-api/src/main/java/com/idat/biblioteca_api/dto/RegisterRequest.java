package com.idat.biblioteca_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List; // Importar esto

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String nombreCompleto;
    
    // Agregamos esto para recibir los roles desde Swagger/Postman
    private List<String> roles; 
}