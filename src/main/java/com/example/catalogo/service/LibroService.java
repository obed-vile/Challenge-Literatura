package com.example.catalogo.service;

import com.example.catalogo.model.Autor;
import com.example.catalogo.model.Libro;
import com.example.catalogo.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ApiGutendexService apiGutendexService; // Servicio para conectar con la API externa

    public Libro guardarLibro(Libro libro) {
        // Verificar si el libro ya existe en la base de datos
        List<Libro> librosExistentes = libroRepository.findByTituloContainingIgnoreCase(libro.getTitulo());
        if (!librosExistentes.isEmpty()) {
            System.out.println("El libro ya está registrado: " + libro.getTitulo());
            return librosExistentes.get(0); // Devuelve el libro existente
        }
        return libroRepository.save(libro); // Guarda el nuevo libro si no existe
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> listarPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro buscarYGuardarLibroPorTitulo(String titulo) {
        try {
            // Buscar en la base de datos
            List<Libro> libros = buscarPorTitulo(titulo);
            if (!libros.isEmpty()) {
                System.out.println("Libro encontrado en la base de datos: " + libros.get(0).getTitulo());
                return libros.get(0); // Devuelve el primer libro encontrado
            }

            // Si no se encuentra en la base de datos, buscar en la API externa
            JsonNode response = apiGutendexService.buscarLibroPorTitulo(titulo);

            if (response.has("results") && response.get("results").size() > 0) {
                JsonNode libroNode = response.get("results").get(0);

                // Crear el objeto Libro con los datos de la API
                Libro libro = new Libro();
                libro.setTitulo(libroNode.get("title").asText());
                libro.setIdioma(libroNode.has("languages") ? libroNode.get("languages").get(0).asText() : "desconocido");

                // Intentar obtener el número de descargas (puede no estar disponible)
                if (libroNode.has("download_count")) {
                    libro.setNumeroDescargas(libroNode.get("download_count").asInt());
                } else {
                    libro.setNumeroDescargas(0); // Valor predeterminado
                }

                // Procesar los datos del autor si están disponibles
                if (libroNode.has("authors") && libroNode.get("authors").size() > 0) {
                    JsonNode autorNode = libroNode.get("authors").get(0); // Usar el primer autor disponible
                    Autor autor = new Autor();
                    autor.setNombre(autorNode.has("name") ? autorNode.get("name").asText() : "Desconocido");
                    autor.setAnioNacimiento(autorNode.has("birth_year") ? autorNode.get("birth_year").asInt() : null);
                    autor.setAnioFallecimiento(autorNode.has("death_year") ? autorNode.get("death_year").asInt() : null);
                    libro.setAutor(autor);
                } else {
                    libro.setAutor(null); // Sin autor si no hay datos en la API
                }

                // Guardar el libro en la base de datos
                Libro libroGuardado = guardarLibro(libro);
                System.out.println("Libro guardado desde la API: " + libroGuardado.getTitulo());
                return libroGuardado;
            } else {
                System.out.println("No se encontró el libro con el título en la API: " + titulo);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al buscar o guardar el libro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
