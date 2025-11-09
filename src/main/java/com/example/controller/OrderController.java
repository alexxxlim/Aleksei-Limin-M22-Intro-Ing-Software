package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.controller.Main;
import com.example.model.Order;
import com.example.model.Searcher;
import com.example.view.OrderView;

public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    //Atributo de clase OrderView
    private OrderView view;

    //Lista de orders
    private List<Order> orders;

    //Atributo de clase Searcher
    private Searcher searcher;

    //Constructor
    public OrderController(OrderView view, List<Order> orders) {
        //Initialize attributes
        this.view = view;
        this.orders = orders;
        this.searcher = new Searcher();

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

        view.displayOrder(foundOrder);
    }
}

