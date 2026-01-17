package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.ExchangeRate;
import com.example.model.Order;
import com.example.model.OrderRepository;
import com.example.view.EditOrderView;
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

        // Listener para editar un pedido
        view.getEditOrderButton().addActionListener(e -> editOrder());

        // Listener para borrar un pedido
        view.getDeleteOrderButton().addActionListener(e -> deleteOrder());
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

        // Calcular total en EUR (usando total con descuento)
        BigDecimal totalEur = BigDecimal.valueOf(foundOrder.getDiscountedTotal());

        // Obtener tipo de cambio desde cache y convertir a USD
        double rateValue = ExchangeRate.getEurUsdRate();
        BigDecimal rate = BigDecimal.valueOf(rateValue);
        BigDecimal totalUsd = totalEur.multiply(rate);
        view.showOrder(foundOrder, totalEur, rate, totalUsd);
    }

    // Método para manejar la selección del pedido desde el combo box
    private void selectOrderFromComboBox() {
        String selectedId = (String) view.getOrderIdComboBox().getSelectedItem();
        
        if (selectedId == null || selectedId.isEmpty() || selectedId.equals("-Not selected-")) {
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

        // Obtener tipo de cambio desde cache y convertir a USD
        double rateValue = ExchangeRate.getEurUsdRate();
        BigDecimal rate = BigDecimal.valueOf(rateValue);
        BigDecimal totalUsd = totalEur.multiply(rate);
        view.showOrder(foundOrder, totalEur, rate, totalUsd);
    }

    // Abrir una nueva ventana para crear pedido
    private void openNewOrderWindow() {
        NewOrderView newOrderView = new NewOrderView();
        // El nuevo controlador usará la misma lista de pedidos y la vista principal
        new NewOrderController(newOrderView, view, orders);
        newOrderView.setVisible(true);
    }

    // Abrir una ventana para editar el pedido actual
    private void editOrder() {
        // Obtener el pedido actualmente mostrado
        Order currentOrder = view.getCurrentOrder();

        // Verificar que hay un pedido seleccionado
        if (currentOrder == null) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, seleccione o busque un pedido para editar.",
                    "No hay pedido seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar diálogo de confirmación con el ID del pedido
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Desea editar el pedido " + currentOrder.getId() + "?",
                "Confirm Edit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Abrir ventana de edición
            EditOrderView editOrderView = new EditOrderView();
            // El controlador usará el pedido actual y la lista de pedidos
            new EditOrderController(editOrderView, view, currentOrder, orders);
            editOrderView.setVisible(true);
        }
    }

    // Borrar un pedido de la lista y del json
    private void deleteOrder() {
        // Determinar el ID del pedido a borrar (prioridad: combo box, sino searchField)
        String orderIdToDelete = null;
        String selectedId = (String) view.getOrderIdComboBox().getSelectedItem();
        
        if (selectedId != null && !selectedId.isEmpty() && !selectedId.equals("-Not selected-")) {
            orderIdToDelete = selectedId;
        } else {
            orderIdToDelete = view.getSearchId();
        }

        // Verificar que se encontró un ID
        if (orderIdToDelete == null || orderIdToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please select or search for an order to delete.",
                    "No Order Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscar el pedido primero para mostrar en el diálogo de confirmación
        Order orderToDelete = null;
        for (Order order : orders) {
            if (order.getId() != null && order.getId().equalsIgnoreCase(orderIdToDelete)) {
                orderToDelete = order;
                break;
            }
        }

        // Verificar que el pedido existe
        if (orderToDelete == null) {
            JOptionPane.showMessageDialog(view,
                    "Order not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar diálogo de confirmación
        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete order " + orderToDelete.getId() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Borrar el pedido usando el repositorio (elimina y guarda en json)
                Order deletedOrder = OrderRepository.deleteOrder(orderIdToDelete, orders);

                if (deletedOrder != null) {
                    // Actualizar combo box con los IDs actualizados
                    refreshOrderIdsInMainView();

                    // Limpiar la vista
                    view.displayOrder(null);
                    view.getSearchField().setText("");
                    view.getOrderIdComboBox().setSelectedItem("-Not selected-");

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(view,
                            "Order " + deletedOrder.getId() + " deleted successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // No debería ocurrir, pero por si acaso
                    JOptionPane.showMessageDialog(view,
                            "Order not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                log.error("Error saving orders to json after deletion: {}", e.getMessage());
                JOptionPane.showMessageDialog(view,
                        "Could not save changes to json file...",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Actualizar el combo box con los IDs de pedidos actualizados
    private void refreshOrderIdsInMainView() {
        if (view == null) {
            return;
        }
        List<String> ids = new ArrayList<>();
        for (Order o : orders) {
            if (o.getId() != null) {
                ids.add(o.getId());
            }
        }
        view.initializeOrderIds(ids);
    }
}

