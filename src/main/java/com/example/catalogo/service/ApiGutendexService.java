package com.example.catalogo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ApiGutendexService {

    private static final String API_URL = "https://gutendex.com/books/";

    public JsonNode buscarLibroPorTitulo(String titulo) throws Exception {
        // Construir la URL con el título como parámetro
        String formattedTitulo = titulo.replace(" ", "%20");
        URL url = new URL(API_URL + "?search=" + formattedTitulo);
        System.out.println("URL construida: " + url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        // Verificar el código de respuesta HTTP
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error al conectar con la API. Código HTTP: " + responseCode);
        }

        // Leer la respuesta usando BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();

        // Convertir el JSON a JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode response = objectMapper.readTree(json.toString());

        // Imprimir la respuesta completa para depuración
        System.out.println("Respuesta JSON de la API:");
        System.out.println(response.toPrettyString());

        return response;
    }
}
