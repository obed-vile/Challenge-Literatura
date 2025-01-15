# 📚 Catálogo de Libros

## 🚀 Descripción

Este proyecto es un catálogo interactivo de libros desarrollado en Java utilizando Spring Boot. Permite a los usuarios buscar libros por título, listar libros registrados, consultar autores vivos en un año específico y buscar libros por idioma. Además, se conecta con la API de Gutendex para obtener información de libros no registrados en la base de datos y los guarda automáticamente si no existen.

![image](https://github.com/user-attachments/assets/2d521773-93bc-4295-b054-de21b2abc825)


## 🚀 Características

- **Búsqueda de libros:** Permite buscar libros por su título.
- **Listado de libros:** Muestra todos los libros registrados en la base de datos.
- **Listado de autores:** Visualiza autores registrados y su información básica.
- **Consulta de autores vivos:** Permite listar autores que estaban vivos en un año específico.
- **Búsqueda por idioma:** Permite listar libros según su idioma, incluyendo opciones como español, inglés, francés, y portugués.
- **Conexión con API externa:** Integra la API de Gutendex para obtener datos actualizados de libros y autores no registrados.
- **Evita duplicados:** No permite registrar libros más de una vez.
- **Manejo de errores:** Incluye validaciones para manejar errores y respuestas de la API.

## 🚀 Requisitos

- **Java:** JDK 17 o superior.
- **Maven:** Para gestionar las dependencias.
- **Base de datos:** PostgreSQL (configurada en `application.properties`).
- **Conexión a Internet:** Para consumir la API de Gutendex.

## 🚀 Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/obed-vile/catalogo-libros.git
