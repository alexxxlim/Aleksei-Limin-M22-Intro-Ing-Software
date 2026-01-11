package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.Article;
import com.example.model.ExchangeRate;
import com.example.model.Order;
import com.example.model.OrderRepository;
import com.example.view.EditOrderView;
import com.example.view.OrderView;

public class EditOrderController {

    private static final Logger log = LoggerFactory.getLogger(EditOrderController.class);

    private EditOrderView view;
    private OrderView mainOrderView;
    private Order orderToEdit;
    private List<Order> orders;

    public EditOrderController(EditOrderView view, OrderView mainOrderView, Order orderToEdit, List<Order> orders) {
        this.view = view;
        this.mainOrderView = mainOrderView;
        this.orderToEdit = orderToEdit;
        this.orders = orders;

        // Inicializar la vista con los datos del pedido
        initializeViewWithOrder();

        // Listener para actualizar totales cuando cambian los valores en la tabla
        view.getArticlesTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    updateTotalsInView();
                }
            }
        });

        // Listener para guardar el pedido
        this.view.getSaveOrderButton().addActionListener(e -> saveOrder());

        // Listener para cancelar
        this.view.getCancelButton().addActionListener(e -> cancel());
    }

    // Inicializar la vista con los datos del pedido actual
    private void initializeViewWithOrder() {
        if (orderToEdit == null || orderToEdit.getArticles() == null || orderToEdit.getArticles().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "El pedido no tiene artículos para editar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            view.dispose();
            return;
        }

        List<Article> articles = orderToEdit.getArticles();
        String[] names = new String[articles.size()];
        Double[] unitPrices = new Double[articles.size()];
        Integer[] quantities = new Integer[articles.size()];
        Double[] discounts = new Double[articles.size()];

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            names[i] = article.getName();
            unitPrices[i] = article.getUnitPrice();
            quantities[i] = article.getQuantity();
            discounts[i] = article.getDiscount();
        }

        view.initializeArticles(names, unitPrices, quantities, discounts);
        updateTotalsInView();
    }

    // Guardar los cambios del pedido
    private void saveOrder() {
        // Validar los datos de la tabla
        if (!validateTableData()) {
            return;
        }

        // Mostrar diálogo de confirmación
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Desea guardar el pedido?",
                "Confirm Save",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Asegurar que la tabla está sincronizada antes de leer valores
        // Forzar que cualquier edición pendiente se complete
        if (view.getArticlesTable().isEditing()) {
            javax.swing.CellEditor editor = view.getArticlesTable().getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
        
        // Actualizar los artículos del pedido con los nuevos valores
        List<Article> updatedArticles = new ArrayList<>();
        log.info("Reading values from table. Row count: {}", view.getRowCount());
        for (int i = 0; i < view.getRowCount(); i++) {
            // Obtener valores originales (name y unitPrice no cambian)
            Article originalArticle = orderToEdit.getArticles().get(i);
            int newQuantity = view.getQuantityAt(i);
            double newDiscount = view.getDiscountAt(i);
            
            log.info("Row {}: Original qty={}, discount={} -> New qty={}, discount={}", 
                    i, originalArticle.getQuantity(), originalArticle.getDiscount(), 
                    newQuantity, newDiscount);

            // Crear nuevo artículo con valores actualizados
            Article updatedArticle = new Article(
                    originalArticle.getName(),
                    newQuantity,
                    originalArticle.getUnitPrice(),
                    newDiscount
            );
            updatedArticles.add(updatedArticle);
        }

        // Buscar y actualizar el pedido en la lista por ID para garantizar que se actualiza el objeto correcto
        String orderId = orderToEdit.getId();
        Order orderInList = null;
        for (Order order : orders) {
            if (order.getId() != null && order.getId().equals(orderId)) {
                orderInList = order;
                break;
            }
        }

        if (orderInList != null) {
            // Log de los artículos antes de actualizar
            log.info("Before update - Order {} has {} articles", orderId, orderInList.getArticles().size());
            for (int i = 0; i < orderInList.getArticles().size() && i < updatedArticles.size(); i++) {
                Article old = orderInList.getArticles().get(i);
                Article newArt = updatedArticles.get(i);
                log.info("Article {}: qty {} -> {}, discount {} -> {}", 
                        i, old.getQuantity(), newArt.getQuantity(), old.getDiscount(), newArt.getDiscount());
            }
            
            // Actualizar el pedido en la lista
            orderInList.setArticles(updatedArticles);
            // Actualizar la referencia para que refreshMainView use el objeto actualizado
            orderToEdit = orderInList;
            log.info("Order {} found in list and updated. Articles count: {}", orderId, updatedArticles.size());
            
            // Verificar que los cambios se aplicaron
            log.info("After update - Order {} has {} articles", orderId, orderInList.getArticles().size());
            for (Article art : orderInList.getArticles()) {
                log.info("Updated article: name={}, qty={}, discount={}", 
                        art.getName(), art.getQuantity(), art.getDiscount());
            }
        } else {
            log.error("Order {} not found in list! This should not happen. Updating orderToEdit directly.", orderId);
            orderToEdit.setArticles(updatedArticles);
        }

        // Verificar que el pedido en la lista tiene los valores actualizados antes de guardar
        if (orderInList != null) {
            log.info("Verifying order in list before save - Order ID: {}", orderId);
            for (int i = 0; i < orderInList.getArticles().size() && i < updatedArticles.size(); i++) {
                Article listArticle = orderInList.getArticles().get(i);
                Article updatedArticle = updatedArticles.get(i);
                if (listArticle.getQuantity() != updatedArticle.getQuantity() || 
                    Math.abs(listArticle.getDiscount() - updatedArticle.getDiscount()) > 0.001) {
                    log.error("MISMATCH: Article {} in list does not match updated article! List: qty={}, disc={}, Updated: qty={}, disc={}", 
                            i, listArticle.getQuantity(), listArticle.getDiscount(), 
                            updatedArticle.getQuantity(), updatedArticle.getDiscount());
                } else {
                    log.info("Verified: Article {} matches - qty={}, disc={}", 
                            i, listArticle.getQuantity(), listArticle.getDiscount());
                }
            }
        }

        // Guardar en el fichero json
        try {
            log.info("Saving {} orders to json file...", orders.size());
            log.info("Order to save - ID: {}, Articles: {}", orderId, orderInList != null ? orderInList.getArticles().size() : orderToEdit.getArticles().size());
            
            // Log de todos los pedidos antes de guardar
            for (Order o : orders) {
                if (o.getId().equals(orderId)) {
                    log.info("Order {} in list before save: {} articles", orderId, o.getArticles().size());
                    for (Article a : o.getArticles()) {
                        log.info("  Article: name={}, qty={}, price={}, discount={}", 
                                a.getName(), a.getQuantity(), a.getUnitPrice(), a.getDiscount());
                    }
                }
            }
            
            OrderRepository.saveOrders(orders);
            log.info("Order {} saved successfully to json file", orderId);
            
            // Verificar que el pedido en la lista sigue teniendo los valores correctos después de guardar
            if (orderInList != null) {
                log.info("Verifying order in list after save - Order ID: {}", orderId);
                for (Article a : orderInList.getArticles()) {
                    log.info("  Article after save: name={}, qty={}, price={}, discount={}", 
                            a.getName(), a.getQuantity(), a.getUnitPrice(), a.getDiscount());
                }
            }

            // Verificar que el archivo se guardó correctamente leyendo el archivo guardado
            try {
                // Intentar encontrar путь к файлу так же, как в OrderRepository
                java.nio.file.Path savedFilePath = null;
                try {
                    java.net.URL resource = com.example.Main.class.getClassLoader().getResource("orders.json");
                    if (resource != null) {
                        java.nio.file.Path loadedPath = java.nio.file.Paths.get(resource.toURI());
                        String loadedPathStr = loadedPath.toFile().getAbsolutePath();
                        if (loadedPathStr.contains("target" + java.io.File.separator + "classes")) {
                            String sourcePathStr = loadedPathStr.replace(
                                "target" + java.io.File.separator + "classes", 
                                "src" + java.io.File.separator + "main" + java.io.File.separator + "resources");
                            savedFilePath = java.nio.file.Paths.get(sourcePathStr);
                        } else if (loadedPathStr.contains("src" + java.io.File.separator + "main" + java.io.File.separator + "resources")) {
                            savedFilePath = java.nio.file.Paths.get(loadedPathStr);
                        }
                    }
                } catch (Exception e) {
                    // Ignorar
                }
                
                if (savedFilePath == null) {
                    savedFilePath = java.nio.file.Paths.get(
                        System.getProperty("user.dir"), "src", "main", "resources", "orders.json");
                }
                
                if (java.nio.file.Files.exists(savedFilePath)) {
                    long fileSize = java.nio.file.Files.size(savedFilePath);
                    log.info("Verified: Saved file exists at {}, size: {} bytes", savedFilePath, fileSize);
                    
                    // Intentar leer el archivo para verificar que contiene el pedido actualizado
                    try {
                        com.fasterxml.jackson.databind.ObjectMapper testMapper = new com.fasterxml.jackson.databind.ObjectMapper()
                                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        java.util.List<com.example.model.Order> savedOrders = testMapper.readValue(
                            savedFilePath.toFile(),
                            testMapper.getTypeFactory().constructCollectionType(
                                java.util.List.class, com.example.model.Order.class));
                        
                        // Buscar el pedido guardado
                        for (com.example.model.Order savedOrder : savedOrders) {
                            if (savedOrder.getId().equals(orderId)) {
                                log.info("Found saved order {} in file with {} articles", orderId, savedOrder.getArticles().size());
                                for (com.example.model.Article savedArt : savedOrder.getArticles()) {
                                    log.info("  Saved article: name={}, qty={}, price={}, discount={}", 
                                            savedArt.getName(), savedArt.getQuantity(), 
                                            savedArt.getUnitPrice(), savedArt.getDiscount());
                                }
                                break;
                            }
                        }
                    } catch (Exception readEx) {
                        log.warn("Could not read saved file to verify: {}", readEx.getMessage());
                    }
                } else {
                    log.warn("Warning: Saved file does not exist at expected location: {}", savedFilePath);
                }
            } catch (Exception verifyEx) {
                log.warn("Could not verify saved file: {}", verifyEx.getMessage());
            }

            // Actualizar la vista principal con el pedido actualizado
            refreshMainView();

            // Cerrar la ventana de edición
            view.dispose();
            
            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(view,
                    "Pedido " + orderId + " guardado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            log.error("Error saving orders to json: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(view,
                    "No se pudo guardar el pedido en el fichero json.\nError: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Validar los datos ingresados en la tabla
    private boolean validateTableData() {
        for (int i = 0; i < view.getRowCount(); i++) {
            int quantity = view.getQuantityAt(i);
            double discount = view.getDiscountAt(i);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(view,
                        "La cantidad debe ser mayor que 0 para el artículo en la fila " + (i + 1) + ".",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (discount < 0 || discount > 100) {
                JOptionPane.showMessageDialog(view,
                        "El descuento debe estar entre 0 y 100 para el artículo en la fila " + (i + 1) + ".",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    // Actualizar totales en la vista basándose en los valores de la tabla
    private void updateTotalsInView() {
        int positions = view.getRowCount();
        double grossTotal = 0.0;
        double discountedTotal = 0.0;

        for (int i = 0; i < view.getRowCount(); i++) {
            // Obtener valores de la tabla
            int quantity = view.getQuantityAt(i);
            double discount = view.getDiscountAt(i);

            // Obtener unitPrice del artículo original (no cambia)
            Article originalArticle = orderToEdit.getArticles().get(i);
            double unitPrice = originalArticle.getUnitPrice();

            // Calcular totales
            double grossAmount = quantity * unitPrice;
            double discountedAmount = grossAmount * (1 - discount / 100.0);

            grossTotal += grossAmount;
            discountedTotal += discountedAmount;
        }

        view.updateTotals(positions, grossTotal, discountedTotal);
    }

    // Actualizar la vista principal con el pedido actualizado
    private void refreshMainView() {
        if (mainOrderView == null) {
            return;
        }

        // Calcular total en EUR
        BigDecimal totalEur = BigDecimal.valueOf(orderToEdit.getDiscountedTotal());

        // Intentar obtener tipo de cambio y mostrar el pedido actualizado
        try {
            BigDecimal rate = ExchangeRate.getCurrentEurUsdRate();
            BigDecimal totalUsd = totalEur.multiply(rate);
            mainOrderView.showOrder(orderToEdit, totalEur, rate, totalUsd);
        } catch (Exception e) {
            log.warn("Failed to get exchange rate: {}", e.getMessage());
            String errorMessage = "No se pudo obtener el tipo de cambio. Error: " + e.getMessage();
            mainOrderView.showOrderWithoutUsd(orderToEdit, totalEur, errorMessage);
        }
    }

    // Cerrar la ventana sin guardar
    private void cancel() {
        view.dispose();
    }
}
