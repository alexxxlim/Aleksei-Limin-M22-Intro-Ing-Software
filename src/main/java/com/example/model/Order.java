package com.example.model;

import java.util.List;

public class Order {

    private String id;
    private List<Article> articles;

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

        int totalNumArticles = 0;
        if (articles != null) {
            for (Article a : articles) {
                totalNumArticles += a.getQuantity();
            }
        }
        sb.append("Número de artículos: ").append(totalNumArticles).append("\n");

        int totalPrecio = 0;
        if (articles != null) {
            for (Article a : articles) {
                totalPrecio += a.getPrice();
            }
        }
        sb.append("Total bruto: ").append(totalPrecio).append("\n");

        sb.append("Total con descuento: ").append(articles != null ? articles.size() : 0).append("\n");
        sb.append("Detalles de artículos: \n");

        if (articles == null || articles.isEmpty()) {
            sb.append("...Error. Sin Artículos... \n");
        } else {
            for (Article article : articles) {
                sb.append(article.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
