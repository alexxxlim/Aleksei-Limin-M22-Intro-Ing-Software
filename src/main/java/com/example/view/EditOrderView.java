package com.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class EditOrderView extends JFrame {

    // Tabla para mostrar y editar artículos
    private JTable articlesTable;
    private DefaultTableModel tableModel;

    // Botones
    private JButton saveOrderButton = new JButton("Save Order");
    private JButton cancelButton = new JButton("Cancel");

    // Totales
    private JTextField positionsCountField = new JTextField(5);
    private JTextField grossTotalField = new JTextField(10);
    private JTextField discountedTotalField = new JTextField(10);

    public EditOrderView() {
        setTitle("Edit Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear modelo de tabla con columnas: Name, Unit Price, Quantity, Discount
        String[] columnNames = {"Name", "Unit Price", "Quantity", "Discount %"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo las columnas Quantity (2) y Discount (3) son editables
                return column == 2 || column == 3;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return String.class; // Name
                } else if (column == 1) {
                    return Double.class; // Unit Price
                } else if (column == 2) {
                    return Integer.class; // Quantity
                } else {
                    return Double.class; // Discount
                }
            }
        };

        articlesTable = new JTable(tableModel);
        articlesTable.setRowHeight(25);

        // Panel principal vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Panel con tabla de artículos
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Edit articles (only Quantity and Discount can be modified):"), BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(articlesTable), BorderLayout.CENTER);

        // Panel de totales (solo info)
        positionsCountField.setEditable(false);
        grossTotalField.setEditable(false);
        discountedTotalField.setEditable(false);

        JPanel totalsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalsPanel.add(new JLabel("Positions:"));
        totalsPanel.add(positionsCountField);
        totalsPanel.add(new JLabel("Total bruto:"));
        totalsPanel.add(grossTotalField);
        totalsPanel.add(new JLabel("Total with discount:"));
        totalsPanel.add(discountedTotalField);

        // Panel de botones de acción
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(saveOrderButton);
        buttonsPanel.add(cancelButton);

        // Construir layout
        mainPanel.add(tablePanel);
        mainPanel.add(totalsPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // centrar la ventana
    }

    // Getters para el controlador
    public JButton getSaveOrderButton() {
        return saveOrderButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    // Inicializar la tabla con los artículos del pedido
    public void initializeArticles(String[] names, Double[] unitPrices, Integer[] quantities, Double[] discounts) {
        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Agregar filas con los datos
        for (int i = 0; i < names.length; i++) {
            Object[] row = {names[i], unitPrices[i], quantities[i], discounts[i]};
            tableModel.addRow(row);
        }
    }

    // Obtener cantidad de una fila
    public int getQuantityAt(int row) {
        Object value = tableModel.getValueAt(row, 2);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    // Obtener descuento de una fila
    public double getDiscountAt(int row) {
        Object value = tableModel.getValueAt(row, 3);
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                String str = ((String) value).trim();
                if (str.isEmpty()) {
                    return 0.0;
                }
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    // Obtener número de filas (artículos)
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    // Actualizar totales en la vista
    public void updateTotals(int positions, double grossTotal, double discountedTotal) {
        positionsCountField.setText(String.valueOf(positions));
        grossTotalField.setText(String.valueOf(grossTotal));
        discountedTotalField.setText(String.valueOf(discountedTotal));
    }

    // Obtener la tabla para agregar listeners si es necesario
    public JTable getArticlesTable() {
        return articlesTable;
    }
}
