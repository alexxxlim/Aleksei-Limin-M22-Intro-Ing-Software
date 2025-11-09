package com.example.model;

public class Article {
    private String name;
    private int quantity;
    private double unitPrice;
    private double discount;

    public Article() {
    }

    public Article(String name, int quantity, double unitPrice, double discount) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
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

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    //funciones
    public double getGrossAmount() {
        Calculator calculator = new Calculator();
        double grossAmount = calculator.multiplyIntDouble(quantity, unitPrice);

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
                "\tCantidad: " + quantity + "\n" +
                "\tPrecio unitario: " + unitPrice + "\n" +
                "\tDescuento: " + discount + "%\n" +
                "\tTotal bruto: " + getGrossAmount() + "\n" +
                "\tTotal con descuento: " + getDiscountedAmount();
    }
}
