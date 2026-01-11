package com.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class NewOrderView extends JFrame {

    // Campos para introducir un artículo
    private JTextField nameField = new JTextField(10);
    private JTextField quantityField = new JTextField(5);
    private JTextField unitPriceField = new JTextField(7);
    private JTextField discountField = new JTextField(5);

    // Botones
    private JButton addArticleButton = new JButton("+");
    private JButton saveOrderButton = new JButton("Save Order");
    private JButton cancelButton = new JButton("Cancel");

    // Lista visual de artículos (solo para mostrar)
    private DefaultListModel<String> articlesListModel = new DefaultListModel<>();
    private JList<String> articlesList = new JList<>(articlesListModel);

    // Totales (solo lectura)
    private JTextField positionsCountField = new JTextField(5);
    private JTextField grossTotalField = new JTextField(10);
    private JTextField discountedTotalField = new JTextField(10);

    public NewOrderView() {
        setTitle("New Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel articleInputPanel = new JPanel();
        articleInputPanel.setLayout(new BoxLayout(articleInputPanel, BoxLayout.Y_AXIS));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("New Article:"));
        articleInputPanel.add(titlePanel);
        
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Qty:"));
        fieldsPanel.add(quantityField);
        fieldsPanel.add(new JLabel("Unit Price:"));
        fieldsPanel.add(unitPriceField);
        fieldsPanel.add(new JLabel("Discount %:"));
        fieldsPanel.add(discountField);
        fieldsPanel.add(addArticleButton); // botón para añadir artículo
        articleInputPanel.add(fieldsPanel);

        // Panel lista de artículos
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Articles in order:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(articlesList), BorderLayout.CENTER);

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
        mainPanel.add(articleInputPanel);
        mainPanel.add(listPanel);
        mainPanel.add(totalsPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // centrar la ventana
    }

    // Getters para el controlador (no lógica aquí)
    public JButton getAddArticleButton() {
        return addArticleButton;
    }

    public JButton getSaveOrderButton() {
        return saveOrderButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public String getArticleNameInput() {
        return nameField.getText().trim();
    }

    public String getQuantityInput() {
        return quantityField.getText().trim();
    }

    public String getUnitPriceInput() {
        return unitPriceField.getText().trim();
    }

    public String getDiscountInput() {
        return discountField.getText().trim();
    }

    // Método para limpiar campos de entrada de artículo
    public void clearArticleInputs() {
        nameField.setText("");
        quantityField.setText("");
        unitPriceField.setText("");
        discountField.setText("");
    }

    // Mostrar un artículo en la lista visual (texto simple)
    public void addArticleToList(String text) {
        articlesListModel.addElement(text);
    }

    // Actualizar totales en la vista
    public void updateTotals(int positions, double grossTotal, double discountedTotal) {
        positionsCountField.setText(String.valueOf(positions));
        grossTotalField.setText(String.valueOf(grossTotal));
        discountedTotalField.setText(String.valueOf(discountedTotal));
    }
}


