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

    private static final String API_URL =
            "https://api.exchangerate.host/latest?base=EUR&symbols=USD";

    // запасной курс, если API не работает (можешь поставить любое адекватное значение)
    private static final BigDecimal FALLBACK_RATE = new BigDecimal("1.08");

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static BigDecimal getCurrentEurUsdRate() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                // если API ответил криво — используем запасной
                System.out.println("WARN: API status code = " + response.statusCode()
                        + ", using fallback rate " + FALLBACK_RATE);
                return FALLBACK_RATE;
            }

            String jsonResponse = response.body();
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.out.println("WARN: Empty API response, using fallback rate " + FALLBACK_RATE);
                return FALLBACK_RATE;
            }

            // можно оставить для отладки, потом убрать
            // System.out.println("DEBUG API response: " + jsonResponse);

            String usdValue = extractUsdFromJson(jsonResponse);
            if (usdValue == null) {
                System.out.println("WARN: Could not extract USD from response, using fallback rate "
                        + FALLBACK_RATE);
                return FALLBACK_RATE;
            }

            try {
                return new BigDecimal(usdValue);
            } catch (NumberFormatException e) {
                System.out.println("WARN: Invalid USD rate format '" + usdValue
                        + "', using fallback rate " + FALLBACK_RATE);
                return FALLBACK_RATE;
            }

        } catch (Exception e) {
            // любая ошибка сети / таймаут → запасной курс
            System.out.println("WARN: Exception while calling API: " + e.getMessage()
                    + ", using fallback rate " + FALLBACK_RATE);
            return FALLBACK_RATE;
        }
    }

    private static String extractUsdFromJson(String json) {
        // Ищем "USD":число (с точкой)
        Pattern pattern = Pattern.compile("\"USD\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}