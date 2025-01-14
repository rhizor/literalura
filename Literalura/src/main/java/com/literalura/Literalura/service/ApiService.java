package com.literalura.Literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.Literalura.model.Book;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApiService {

    private static final String BASE_URL = "https://gutendex.com/books";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Book> fetchBooks(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = BASE_URL + "?search=" + encodedQuery;

        System.out.println("URL de solicitud: " + url);

        try {
            // Configuramos el cliente para seguir redirecciones
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado de la respuesta final: " + response.statusCode());

            System.out.println("Respuesta completa: " + response.body());

            JsonNode root = mapper.readTree(response.body());

            JsonNode results = root.get("results");
            if (results == null || !results.isArray()) {
                System.out.println("No se encontraron resultados para la búsqueda.");
                return Collections.emptyList();
            }

            List<Book> books = new ArrayList<>();
            for (JsonNode bookNode : results) {
                try {
                    Book book = mapper.treeToValue(bookNode, Book.class);
                    books.add(book);
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing JSON for a book: " + e.getMessage());
                }
            }
            return books;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al hacer la solicitud a la API: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // o manejar el error de otra manera
        }
    }
}