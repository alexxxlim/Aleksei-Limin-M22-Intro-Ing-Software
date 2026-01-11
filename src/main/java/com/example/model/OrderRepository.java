package com.example.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Main;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderRepository {

    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    private static final String ORDERS_FILE_NAME = "orders.json";

    // Configurar mapper para ignorar propiedades desconocidas (como grossAmount que se calcula)
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Cargar lista de pedidos desde el json
    public static List<Order> loadOrders() throws IOException {
        URL resource = Main.class.getClassLoader().getResource(ORDERS_FILE_NAME);
        if (resource == null) {
            log.error("...Error. file:\"{}\" not found...", ORDERS_FILE_NAME);
            throw new IOException("Orders file not found");
        }

        try {
            Path path = Paths.get(resource.toURI());
            File file = path.toFile();
            List<Order> orders = mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Order.class));
            log.info("Orders loaded from json: {}", orders.size());
            return orders;
        } catch (URISyntaxException e) {
            log.error("Error converting resource URL to URI: {}", e.getMessage());
            throw new IOException("Cannot read orders file", e);
        }
    }

    // Guardar lista de pedidos en el json
    public static void saveOrders(List<Order> orders) throws IOException {
        String projectRoot = System.getProperty("user.dir");
        Path sourceFilePath = Paths.get(projectRoot, "src", "main", "resources", ORDERS_FILE_NAME);
        File file = sourceFilePath.toFile();

        // Verificar que el directorio existe
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            log.error("Directory does not exist: {}", parentDir.getAbsolutePath());
            throw new IOException("Resources directory not found: " + parentDir.getAbsolutePath());
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, orders);
            log.info("Orders saved successfully to {} . Total: {}", file.getAbsolutePath(), orders.size());
        } catch (IOException e) {
            log.error("Error writing to file {}: {}", file.getAbsolutePath(), e.getMessage());
            throw new IOException("Cannot write orders file: " + e.getMessage(), e);
        }
    }

    // Borrar un pedido por ID y guardar la lista actualizada en el json
    public static Order deleteOrder(String orderId, List<Order> orders) throws IOException {
        if (orderId == null || orderId.isEmpty()) {
            return null;
        }

        // Buscar el pedido en la lista
        Order orderToDelete = null;
        for (Order order : orders) {
            if (order.getId() != null && order.getId().equalsIgnoreCase(orderId)) {
                orderToDelete = order;
                break;
            }
        }

        // Si no se encuentra, retornar null
        if (orderToDelete == null) {
            return null;
        }

        // Eliminar el pedido de la lista
        orders.remove(orderToDelete);

        // Guardar la lista actualizada en el json
        saveOrders(orders);
        log.info("Order {} deleted successfully", orderToDelete.getId());

        return orderToDelete;
    }
}