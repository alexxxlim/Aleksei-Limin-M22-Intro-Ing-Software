package com.example.view;

import javax.swing.*;

import com.example.model.Order;

import java.awt.*;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderView extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(OrderView.class);
    
    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");
    private JTextArea resultArea = new JTextArea(10, 40);

    public OrderView() {
        setTitle("Order Management");
        setAppIcon();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Order ID:"));
        add(searchField);
        add(searchButton);
        add(new JScrollPane(resultArea));

        pack();
        setVisible(true);
    }

    public String getSearchId() {
        return searchField.getText().trim();
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public void displayOrder(Order order) {
        if (order == null) {
            resultArea.setText("Order not found...");
            return;
        }
        resultArea.setText(order.toString());
    }

    public void showOrder(Order order, BigDecimal totalEur, BigDecimal rate, BigDecimal totalUsd) {
        if (order == null) {
            resultArea.setText("Order not found...");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(order.toString());
        sb.append("\n\n--- Conversión de Moneda ---\n");
        sb.append("Total en EUR: ").append(totalEur).append(" EUR\n");
        sb.append("Tipo de cambio EUR/USD: ").append(rate).append("\n");
        sb.append("Total en USD: ").append(totalUsd).append(" USD\n");

        resultArea.setText(sb.toString());
    }

    public void showOrderWithoutUsd(Order order, BigDecimal totalEur, String warning) {
        if (order == null) {
            resultArea.setText("Order not found...");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(order.toString());
        sb.append("\n--- Conversión de Moneda ---\n");
        sb.append("Total en EUR: ").append(totalEur).append(" EUR\n");
        sb.append("ADVERTENCIA: ").append(warning).append("\n");

        resultArea.setText(sb.toString());
    }

    private void setAppIcon() {
        java.net.URL iconUrl = getClass().getResource("/app.png");
        if (iconUrl == null) {
            // Alternative loading method attempt
            iconUrl = getClass().getClassLoader().getResource("app.png");
        }
        
        if (iconUrl != null) {
            Image image = Toolkit.getDefaultToolkit().getImage(iconUrl);
            setIconImage(image);
            
            try {
                if (Taskbar.isTaskbarSupported()) {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(image);
                        log.info("Application icon set via Taskbar API");
                    }
                }
            } catch (Exception e) {
                log.debug("Taskbar API not available: {}", e.getMessage());
            }
            
            log.info("Application icon loaded successfully from: {}", iconUrl);
        } else {
            log.warn("Application icon 'app.png' not found in resources");
        }
    }
}
