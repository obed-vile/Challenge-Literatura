package com.example.catalogo.repository;

import com.example.catalogo.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Consulta para listar autores vivos en un año determinado
    List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(int anioInicio, int anioFin);

    // Consulta para buscar un autor por nombre
    Optional<Autor> findByNombre(String nombre);
}
