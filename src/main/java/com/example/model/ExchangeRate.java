package com.example.model;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExchangeRate {

    // Cache del tipo de cambio
    private static double eurUsdRate = 1.0;
    private static boolean loaded = false;

    private static final String API_URL =
            "https://api.exchangerate-api.com/v4/latest/EUR";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Se carga una vez al iniciar la app
    public static void init() {
        try {
            BigDecimal rate = fetchCurrentEurUsdRate();
            eurUsdRate = rate.doubleValue();
            loaded = true;
            System.out.println("Tipo de cambio EUR/USD cargado: " + eurUsdRate);
        } catch (Exception e) {
            // Fallback si falla la API
            eurUsdRate = 1.0;
            loaded = false;
            System.out.println("Error al cargar tipo de cambio. Se usa 1.0 por defecto.");
        }
    }

    // Retorna el tipo de cambio desde el cache
    public static double getEurUsdRate() {
        if (!loaded) {
            return 1.0;
        }
        return eurUsdRate;
    }

    // Verifica si el tipo de cambio fue cargado correctamente
    public static boolean isLoaded() {
        return loaded;
    }

    // Método legacy: retorna BigDecimal desde el cache (sin HTTP)
    public static BigDecimal getCurrentEurUsdRate() {
        return BigDecimal.valueOf(getEurUsdRate());
    }

    // Método privado que hace el HTTP request (solo se llama desde init)
    private static BigDecimal fetchCurrentEurUsdRate() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API returned status code: " + response.statusCode());
        }

        String jsonResponse = response.body();
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            throw new RuntimeException("API returned empty response");
        }

        String usdValue = extractUsdFromJson(jsonResponse);
        if (usdValue == null) {
            throw new RuntimeException("Could not extract USD rate from API response: " + jsonResponse);
        }

        try {
            return new BigDecimal(usdValue);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid USD rate format: " + usdValue, e);
        }
    }

    private static String extractUsdFromJson(String json) {
        // API exchangerate-api.com devuelve: {"rates": {"USD": 1.08, ...}, "base": "EUR", "date": "..."}
        // Intentar diferentes patrones para diferentes formatos de respuesta
        Pattern[] patterns = {
            // Patrón para formato: "rates": {"USD": 1.08, ...}
            Pattern.compile("\"rates\"\\s*:\\s*\\{[^}]*\"USD\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)", Pattern.CASE_INSENSITIVE),
            // Patrón para formato: "USD": 1.08 (directamente en la raíz)
            Pattern.compile("\"USD\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)", Pattern.CASE_INSENSITIVE),
            // Patrón alternativo con comillas
            Pattern.compile("\"USD\"\\s*:\\s*\"?(-?[0-9]+(?:\\.[0-9]+)?)\"?", Pattern.CASE_INSENSITIVE)
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return null;
    }
}