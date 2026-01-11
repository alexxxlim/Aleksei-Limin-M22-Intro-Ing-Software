package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.Article;
import com.example.model.Order;
import com.example.model.OrderRepository;
import com.example.view.NewOrderView;
import com.example.view.OrderView;

public class NewOrderController {

    private static final Logger log = LoggerFactory.getLogger(NewOrderController.class);

    private NewOrderView view;
    private OrderView mainOrderView;
    private List<Order> orders;
    private List<Article> articles = new ArrayList<>();

    public NewOrderController(NewOrderView view, OrderView mainOrderView, List<Order> orders) {
        this.view = view;
        this.mainOrderView = mainOrderView;
        this.orders = orders;

        // Listener para añadir un artículo
        this.view.getAddArticleButton().addActionListener(e -> addArticle());

        // Listener para guardar el pedido
        this.view.getSaveOrderButton().addActionListener(e -> saveOrder());

        // Listener para cancelar
        this.view.getCancelButton().addActionListener(e -> cancel());
    }

    // Añadir artículo a la lista interna y actualizar la vista
    private void addArticle() {
        String name = view.getArticleNameInput();
        String qtyText = view.getQuantityInput();
        String unitPriceText = view.getUnitPriceInput();
        String discountText = view.getDiscountInput();

        if (name.isEmpty() || qtyText.isEmpty() || unitPriceText.isEmpty() || discountText.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos de artículo son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            double unitPrice = Double.parseDouble(unitPriceText);
            double discount = Double.parseDouble(discountText);

            if (quantity <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser positiva");
            }
            if (discount < 0 || discount > 100) {
                throw new IllegalArgumentException("Descuento debe estar entre 0 y 100");
            }

            Article article = new Article(name, quantity, unitPrice, discount);
            articles.add(article);

            String text = "name:" + name + " / qty:" + quantity + " / unit price:" + unitPrice + " / discount:" + discount + " / total bruto:" + article.getGrossAmount() + " / discounted total:" + article.getDiscountedAmount();
            view.addArticleToList(text);

            // Actualizar totales en la vista
            updateTotalsInView();

            // Limpiar campos de entrada
            view.clearArticleInputs();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Cantidad, precio y descuento deben ser numéricos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Guardar el pedido en memoria y en json
    private void saveOrder() {
        if (articles.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "El pedido debe tener al menos un artículo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newId = generateNewOrderId();
        Order newOrder = new Order(newId, new ArrayList<>(articles));
        orders.add(newOrder);

        try {
            // Guardar en el fichero json
            OrderRepository.saveOrders(orders);
            log.info("New order created with id {}", newId);

            // Actualizar combo de ids en la vista principal
            refreshOrderIdsInMainView();

            JOptionPane.showMessageDialog(view,
                    "Pedido guardado correctamente con id: " + newId,
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

            view.dispose();
        } catch (IOException e) {
            log.error("Error saving orders to json: {}", e.getMessage());
            JOptionPane.showMessageDialog(view,
                    "No se pudo guardar el pedido en el fichero json.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generar un id nuevo en formato O000, O001, Oxxx... basado en los ids existentes
    private String generateNewOrderId() {
        int maxNumeric = 0;
        for (Order order : orders) {
            String id = order.getId();
            if (id == null || id.isEmpty()) {
                continue;
            }
            if (id.startsWith("O") && id.length() > 1) {
                try {
                    String numericPart = id.substring(1); // Todo después de "O"
                    int value = Integer.parseInt(numericPart);
                    if (value > maxNumeric) {
                        maxNumeric = value;
                    }
                } catch (NumberFormatException ex) {
                    log.debug("ID {} no tiene formato numérico después de O, ignorado", id);
                }
            }
        }
        int next = maxNumeric + 1;
        // Formato O000, O001, Oxxx...
        return String.format("O%03d", next);
    }

    // Actualizar totales (posiciones, total bruto, total con descuento)
    private void updateTotalsInView() {
        int positions = articles.size();
        double grossTotal = 0.0;
        double discountedTotal = 0.0;

        for (Article a : articles) {
            grossTotal += a.getGrossAmount();
            discountedTotal += a.getDiscountedAmount();
        }

        view.updateTotals(positions, grossTotal, discountedTotal);
    }

    // Rellenar de nuevo el combo de ids en la ventana principal
    private void refreshOrderIdsInMainView() {
        if (mainOrderView == null) {
            return;
        }
        List<String> ids = new ArrayList<>();
        for (Order o : orders) {
            if (o.getId() != null) {
                ids.add(o.getId());
            }
        }
        mainOrderView.initializeOrderIds(ids);
    }

    // Cerrar la ventana sin guardar
    private void cancel() {
        view.dispose();
    }
}



