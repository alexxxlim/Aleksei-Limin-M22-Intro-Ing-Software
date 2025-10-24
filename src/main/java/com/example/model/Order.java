package com.example.model;

import java.util.List;

public class Order {

    private String id;
    private List<Article> articles;

    public Order() {
    }

    public Order(String id, List<Article> articles) {
        this.id = id;
        this.articles = articles;
    }

    public String getId() {
        return id;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public double getGrossTotal() {
        if (articles == null || articles.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        for (Article article : articles) {
            total += article.getGrossAmount();
        }

        return total;
    }

    public double getDiscountedTotal() {
        if (articles == null || articles.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        for (Article article : articles) {
            total += article.getDiscountedAmount();
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Order ID: ").append(id).append("\n");

        int numPosiciones = (articles != null) ? articles.size() : 0;
        sb.append("Número de posiciones: ").append(numPosiciones).append("\n");

        int totalUnidades = 0;
        if (articles != null) {
            for (Article a : articles) {
                totalUnidades += a.getQuantity();
            }
        }
        sb.append("Cantidad total de unidades: ").append(totalUnidades).append("\n");

        sb.append("Valor total bruto: ").append(getGrossTotal()).append("\n");
        sb.append("Valor total con descuento: ").append(getDiscountedTotal()).append("\n");

        sb.append("Detalles de artículos:\n");
        if (articles != null && !articles.isEmpty()) {
            for (Article article : articles) {
                sb.append(article.toString()).append("\n");
            }
        } else {
            sb.append("...Error. No hay artículos... \n");
        }

        return sb.toString();
    }
}
