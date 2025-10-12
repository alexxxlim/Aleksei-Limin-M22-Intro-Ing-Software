package com.example;

import com.example.model.Article;
import com.example.model.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("Starting Order Managment System. . .");

        Article articleRaton = new Article("Ratón", 13, 10.99, 15.00);

        System.out.println(articleRaton.getGrossAmount());
        System.out.println(articleRaton.getDiscountedAmount());

        Article articleTeclado = new Article("Teclado", 7, 23.75, 10.00);

        List<Article> articles1 = Arrays.asList(articleRaton, articleTeclado);
        Order order1 = new Order("A001", articles1);
        System.out.println(order1.toString());
    }
}