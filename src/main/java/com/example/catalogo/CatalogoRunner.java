package com.example.catalogo;

import com.example.catalogo.model.Autor;
import com.example.catalogo.model.Libro;
import com.example.catalogo.repository.AutorRepository;
import com.example.catalogo.repository.LibroRepository;
import com.example.catalogo.service.ApiGutendexService;
import com.example.catalogo.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CatalogoRunner implements CommandLineRunner {

    @Autowired
    private ApiGutendexService apiGutendexService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    public void mostrarMenu() {
        System.out.println("\nElija la opción a través de su número:");
        System.out.println("1- buscar libro por título");
        System.out.println("2- listar libros registrados");
        System.out.println("3- listar autores registrados");
        System.out.println("4- listar autores vivos en un determinado año");
        System.out.println("5- listar libros por idioma");
        System.out.println("0- salir");
        System.out.print("\nSeleccione una opción: ");
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            mostrarMenu();

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1: // Buscar libro por título
                    System.out.print("Ingrese el nombre del libro que desea buscar: ");
                    String titulo = scanner.nextLine();
                    Libro libro = libroService.buscarYGuardarLibroPorTitulo(titulo);
                    if (libro != null) {
                        System.out.println("\n----- LIBRO -----");
                        System.out.println("Título: " + libro.getTitulo());
                        if (libro.getAutor() != null) {
                            System.out.println("Autor: " + libro.getAutor().getNombre());
                        } else {
                            System.out.println("Autor: Desconocido");
                        }
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                        System.out.println("----------------");
                    } else {
                        System.out.println("No se encontró el libro.");
                    }
                    break;

                case 2: // Listar todos los libros
                    List<Libro> libros = libroRepository.findAll();
                    if (libros.isEmpty()) {
                        System.out.println("No hay libros registrados.");
                    } else {
                        System.out.println("\n----- LIBROS REGISTRADOS -----");
                        libros.forEach(l -> {
                            System.out.println("Título: " + l.getTitulo());
                            if (l.getAutor() != null) {
                                System.out.println("Autor: " + l.getAutor().getNombre());
                            } else {
                                System.out.println("Autor: Desconocido");
                            }
                            System.out.println("Idioma: " + l.getIdioma());
                            System.out.println("Número de descargas: " + l.getNumeroDescargas());
                            System.out.println("----------------");
                        });
                    }
                    break;

                case 3: // Listar todos los autores
                    List<Autor> autores = autorRepository.findAll();
                    if (autores.isEmpty()) {
                        System.out.println("No hay autores registrados.");
                    } else {
                        System.out.println("\n----- AUTORES REGISTRADOS -----");
                        autores.forEach(a -> {
                            System.out.println("Nombre: " + a.getNombre());
                            System.out.println("Año de Nacimiento: " + a.getAnioNacimiento());
                            System.out.println("Año de Fallecimiento: " +
                                    (a.getAnioFallecimiento() != null ? a.getAnioFallecimiento() : "Vivo"));
                            System.out.println("----------------");
                        });
                    }
                    break;

                case 4: // Listar autores vivos en un determinado año
                    System.out.print("Ingrese el año para consultar autores vivos: ");
                    int anio = scanner.nextInt();
                    List<Autor> autoresVivos = autorRepository.findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(anio, anio);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("No se encontraron autores vivos en el año " + anio);
                    } else {
                        System.out.println("\n----- AUTORES VIVOS EN " + anio + " -----");
                        autoresVivos.forEach(a -> {
                            System.out.println("Nombre: " + a.getNombre());
                            System.out.println("Año de Nacimiento: " + a.getAnioNacimiento());
                            System.out.println("----------------");
                        });
                    }
                    break;

                case 5: // Listar libros por idioma
                    System.out.println("Ingrese el idioma para buscar los libros:");
                    System.out.println("es- español");
                    System.out.println("en- inglés");
                    System.out.println("fr- francés");
                    System.out.println("pt- portugués");
                    System.out.print("Seleccione un idioma (por código): ");
                    String idiomaSeleccionado = scanner.nextLine();

                    // Validar el idioma seleccionado
                    if (!List.of("es", "en", "fr", "pt").contains(idiomaSeleccionado)) {
                        System.out.println("Idioma no válido. Inténtalo de nuevo.");
                        break;
                    }

                    List<Libro> librosPorIdioma = libroRepository.findByIdioma(idiomaSeleccionado);
                    if (librosPorIdioma.isEmpty()) {
                        System.out.println("No se encontraron libros en el idioma: " + idiomaSeleccionado);
                    } else {
                        System.out.println("\n----- LIBROS EN IDIOMA " + idiomaSeleccionado + " -----");
                        librosPorIdioma.forEach(l -> {
                            System.out.println("Título: " + l.getTitulo());
                            System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido"));
                            System.out.println("Número de descargas: " + l.getNumeroDescargas());
                            System.out.println("----------------");
                        });
                    }
                    break;

                case 0: // Salir
                    System.out.println("Gracias por usar el catálogo de libros. ¡Hasta pronto!");
                    return;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
    }
}
