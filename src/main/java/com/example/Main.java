package com.example;

import com.example.controller.*;
import com.example.model.*;
import com.example.view.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.io.InputStream;
import java.io.IOException;

import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        log.info("Starting Order Managment System. . .");
        System.out.println();

        InputStream file = Main.class.getClassLoader().getResourceAsStream("orders.json");
        if (file == null) {
            log.error("...Error. file:\"orders.json\" not found...");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Order> orders = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Order.class));

        for (Order order : orders) {
            log.debug("Loaded order: {}", order.getId());
        }

        log.info("All orders loaded: {}", orders.size());

        //Initialize MVC
        SwingUtilities.invokeLater(() -> {
            OrderView view = new OrderView();
            new OrderController(view, orders);
        });
    }
}