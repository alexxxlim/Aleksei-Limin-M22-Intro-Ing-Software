package com.example;

import java.util.List;

public class Order {
    public String id;
    public List<Article> articles;

    public Order(String id){

        this.id = id;
    }
}
