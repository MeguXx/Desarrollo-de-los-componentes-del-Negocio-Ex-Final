package com.idat.biblioteca_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    private String isbn;

    private Integer anioPublicacion;

    @Column(nullable = false)
    private Integer ejemplaresTotales;

    @Column(nullable = false)
    private Integer ejemplaresDisponibles;
}
