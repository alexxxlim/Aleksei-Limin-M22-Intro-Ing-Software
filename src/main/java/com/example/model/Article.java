package com.example.model;

import com.example.Calculator;

public class Article {
    private String name;
    private int quantity;
    private double price;
    private double discount;

    public Article(String name, int quantity, double price, double discount) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getGrossAmount() {
        Calculator calculator = new Calculator();
        double grossAmount = calculator.multiplyIntDouble(quantity, price);

        return grossAmount;
    }

    public double getDiscountedAmount() {
        Calculator calculator = new Calculator();
        double discountedAmount = calculator.discount(getGrossAmount(), discount);

        return discountedAmount;
    }

    @Override
    public String toString() {
        return "- Artículo: " + name + "\n" +
                "    Cantidad: " + quantity + "\n" +
                "    Precio unitario: " + price + "\n" +
                "    Descuento: " + discount + "%\n" +
                "    Total bruto: " + getGrossAmount() + "\n" +
                "    Total con descuento: " + getDiscountedAmount();
    }
}
