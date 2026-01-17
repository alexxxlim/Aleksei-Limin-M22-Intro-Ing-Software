package com.example;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.controller.OrderController;
import com.example.model.ExchangeRate;
import com.example.model.Order;
import com.example.model.OrderRepository;
import com.example.view.OrderView;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        log.info("Starting Order Managment System. . .");
        System.out.println();

        List<Order> orders = OrderRepository.loadOrders();

        for (Order order : orders) {
            log.debug("Loaded order: {}", order.getId());
        }

        log.info("All orders loaded: {}", orders.size());

        // Cargar tipo de cambio una vez al iniciar
        ExchangeRate.init();
        if (!ExchangeRate.isLoaded()) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo cargar el tipo de cambio. Se usa 1.0 por defecto.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }

        //Initialize MVC
        SwingUtilities.invokeLater(() -> {
            OrderView view = new OrderView();
            new OrderController(view, orders);
        });
    }
}