package com.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.Order;

public class OrderView extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(OrderView.class);
    
    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");
    private JComboBox<String> orderIdComboBox = new JComboBox<>();
    private JButton addOrderButton = new JButton("Add New Order");
    private JTextArea resultArea = new JTextArea(10, 40);
    private JButton deleteOrderButton = new JButton("Delete Order");

    public OrderView() {
        setTitle("Order Management");
        setAppIcon();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel izquierdo con búsqueda y lista
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Order ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Panel de lista de pedidos
        JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listPanel.add(new JLabel("Orders List: "));
        listPanel.add(orderIdComboBox);

        // Botón para crear un nuevo pedido (debajo de la lista)
        JPanel addOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addOrderPanel.add(addOrderButton);

        // Agregar paneles al panel izquierdo
        leftPanel.add(searchPanel);
        leftPanel.add(listPanel);
        leftPanel.add(addOrderPanel);

        // Panel central con área de resultado y botón de borrado
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Área de texto con scroll
        centerPanel.add(new JScrollPane(resultArea));
        
        // Panel con botón de borrado (debajo del área de texto)
        JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteButtonPanel.add(deleteOrderButton);
        centerPanel.add(deleteButtonPanel);

        // Agregar todo al frame principal
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public String getSearchId() {
        return searchField.getText().trim();
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JComboBox<String> getOrderIdComboBox() {
        return orderIdComboBox;
    }

    public JButton getAddOrderButton() {
        return addOrderButton;
    }

    public JButton getDeleteOrderButton() {
        return deleteOrderButton;
    }

    // Inicializa el combo box con la lista de IDs de pedidos
    public void initializeOrderIds(List<String> orderIds) {
        orderIdComboBox.removeAllItems();
        // Agregar opción por defecto "-Not selected-"
        orderIdComboBox.addItem("-Not selected-");
        for (String id : orderIds) {
            orderIdComboBox.addItem(id);
        }
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
