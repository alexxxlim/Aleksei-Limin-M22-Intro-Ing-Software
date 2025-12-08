package com.example.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.Order;
import com.example.model.ExchangeRate;
import com.example.view.OrderView;

public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    //Atributo de clase OrderView
    private OrderView view;

    //Lista de orders
    private List<Order> orders;

    //Constructor
    public OrderController(OrderView view, List<Order> orders) {
        //Initialize attributes
        this.view = view;
        this.orders = orders;

        //Adding action listener to the search button, call searchOrder on click
        view.getSearchButton().addActionListener(e -> searchOrder());
        //p.s.: Me vi obligado a modificar la lógica del patrón MVC para cumplir con las especificaciones técnicas
    }

    private void searchOrder() {
        String id = view.getSearchId();

        if (id == null || id.isEmpty()) {
            view.displayOrder(null);
            return;
        }

        // Display order if found, else display "Order not found..."
        Order foundOrder = null;
        for (Order order : orders) {
            if (order.getId() != null && order.getId().equalsIgnoreCase(id)) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder == null) {
            view.displayOrder(null);
            return;
        }

        // Calculate total in EUR (using discounted total)
        BigDecimal totalEur = BigDecimal.valueOf(foundOrder.getDiscountedTotal());

        // Get exchange rate and convert to USD
        try {
            BigDecimal rate = ExchangeRate.getCurrentEurUsdRate();
            BigDecimal totalUsd = totalEur.multiply(rate);
            view.showOrder(foundOrder, totalEur, rate, totalUsd);
        } catch (Exception e) {
            log.warn("Failed to get exchange rate: {}", e.getMessage());
            String errorMessage = "No se pudo obtener el tipo de cambio. Error: " + e.getMessage();
            view.showOrderWithoutUsd(foundOrder, totalEur, errorMessage);
        }
    }
}

