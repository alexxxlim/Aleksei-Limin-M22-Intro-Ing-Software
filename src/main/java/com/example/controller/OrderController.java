package com.example.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.ExchangeRate;
import com.example.model.Order;
import com.example.view.NewOrderView;
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

        // Inicializar combo box con IDs de pedidos
        List<String> orderIds = new ArrayList<>();
        for (Order order : orders) {
            if (order.getId() != null) {
                orderIds.add(order.getId());
            }
        }
        view.initializeOrderIds(orderIds);

        //Adding action listener to the search button, call searchOrder on click
        view.getSearchButton().addActionListener(e -> searchOrder());
        
        // Agregar listener al combo box para seleccionar pedido
        view.getOrderIdComboBox().addActionListener(e -> selectOrderFromComboBox());

        // Listener para abrir la ventana de nuevo pedido
        view.getAddOrderButton().addActionListener(e -> openNewOrderWindow());
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

    // Método para manejar la selección del pedido desde el combo box
    private void selectOrderFromComboBox() {
        String selectedId = (String) view.getOrderIdComboBox().getSelectedItem();
        
        if (selectedId == null || selectedId.isEmpty() || selectedId.equals("-No seleccionado-")) {
            view.displayOrder(null);
            return;
        }

        // Buscar el pedido seleccionado
        Order foundOrder = null;
        for (Order order : orders) {
            if (order.getId() != null && order.getId().equalsIgnoreCase(selectedId)) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder == null) {
            view.displayOrder(null);
            return;
        }

        // Calcular total en EUR (usando total con descuento)
        BigDecimal totalEur = BigDecimal.valueOf(foundOrder.getDiscountedTotal());

        // Obtener tipo de cambio y convertir a USD
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

    // Abrir una nueva ventana para crear pedido
    private void openNewOrderWindow() {
        NewOrderView newOrderView = new NewOrderView();
        // El nuevo controlador usará la misma lista de pedidos y la vista principal
        new NewOrderController(newOrderView, view, orders);
        newOrderView.setVisible(true);
    }
}

