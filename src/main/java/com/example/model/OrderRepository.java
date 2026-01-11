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
        // Intentar encontrar el archivo fuente en src/main/resources
        // Primero intentar usar el path del archivo cargado como referencia
        File sourceFile = null;
        try {
            URL resource = Main.class.getClassLoader().getResource(ORDERS_FILE_NAME);
            if (resource != null) {
                Path loadedPath = Paths.get(resource.toURI());
                File loadedFile = loadedPath.toFile();
                String loadedPathStr = loadedFile.getAbsolutePath();
                
                // Si el archivo está en target/classes, encontrar el archivo fuente en src/main/resources
                if (loadedPathStr.contains("target" + File.separator + "classes")) {
                    // Reemplazar target/classes con src/main/resources
                    String sourcePathStr = loadedPathStr.replace(
                        "target" + File.separator + "classes", 
                        "src" + File.separator + "main" + File.separator + "resources");
                    sourceFile = new File(sourcePathStr);
                    log.info("Detected target/classes path, using source path: {}", sourceFile.getAbsolutePath());
                } else if (loadedPathStr.contains("src" + File.separator + "main" + File.separator + "resources")) {
                    // Ya está en src/main/resources
                    sourceFile = loadedFile;
                    log.info("File already in src/main/resources: {}", sourceFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("Could not determine source file from loaded resource: {}", e.getMessage());
        }
        
        // Si no se pudo determinar desde el archivo cargado, usar user.dir
        if (sourceFile == null || !sourceFile.exists()) {
            String projectRoot = System.getProperty("user.dir");
            Path sourceFilePath = Paths.get(projectRoot, "src", "main", "resources", ORDERS_FILE_NAME);
            sourceFile = sourceFilePath.toFile();
            log.info("Using user.dir to determine path: {}", sourceFile.getAbsolutePath());
        }
        
        File file = sourceFile;

        log.info("Attempting to save orders to: {}", file.getAbsolutePath());
        log.info("File exists before save: {}", file.exists());
        log.info("Parent directory exists: {}", file.getParentFile().exists());
        log.info("Can write to file: {}", file.exists() ? file.canWrite() : file.getParentFile().canWrite());

        // Si el archivo fuente no existe, intentar crear el directorio
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                log.error("Could not create directory: {}", parentDir.getAbsolutePath());
                throw new IOException("Resources directory not found and could not be created: " + parentDir.getAbsolutePath());
            }
            log.info("Created directory: {}", parentDir.getAbsolutePath());
        }

        // Verificar que podemos escribir en el archivo
        if (file.exists() && !file.canWrite()) {
            log.error("File exists but is not writable: {}", file.getAbsolutePath());
            throw new IOException("File is not writable: " + file.getAbsolutePath());
        }

        try {
            long sizeBefore = file.exists() ? file.length() : 0;
            
            // Escribir el archivo
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, orders);
            
            // Verificar que el archivo se escribió correctamente
            if (!file.exists()) {
                log.error("File does not exist after write: {}", file.getAbsolutePath());
                throw new IOException("File was not created");
            }
            
            if (file.length() == 0) {
                log.error("File is empty after write: {}", file.getAbsolutePath());
                throw new IOException("File was written but is empty");
            }
            
            long sizeAfter = file.length();
            log.info("Orders saved successfully to {} . Total: {} orders, File size: {} bytes (was: {} bytes)", 
                    file.getAbsolutePath(), orders.size(), sizeAfter, sizeBefore);
        } catch (IOException e) {
            log.error("Error writing to file {}: {}", file.getAbsolutePath(), e.getMessage(), e);
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