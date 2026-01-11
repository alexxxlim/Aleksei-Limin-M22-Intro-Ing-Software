# Proyecto Java Maven - Sistema de Gestión de Pedidos

Este proyecto es una aplicación completa de gestión de pedidos desarrollada en Java utilizando Maven, Swing (JFrame) y arquitectura MVC (Modelo-Vista-Controlador). La aplicación permite crear, visualizar, editar y eliminar pedidos, calcular totales con descuentos, y realizar conversión de moneda EUR a USD utilizando una API externa.

## Tabla de Contenidos

- [Introducción](#introducción)
- [Requisitos](#requisitos)
- [Configuración del Entorno](#configuración-del-entorno)
- [Compilación y Ejecución](#compilación-y-ejecución)
- [Manual de Usuario](#manual-de-usuario)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Arquitectura y Tecnologías](#arquitectura-y-tecnologías)
- [Contribución](#contribución)
- [Licencia](#licencia)

---

## Introducción

Sistema de gestión de pedidos desarrollado en **Java 17** con **Maven**, que implementa una aplicación de escritorio con interfaz gráfica Swing. Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre pedidos con sus artículos, cálculos automáticos de totales y conversión de moneda EUR a USD en tiempo real.

### Características Principales

- Crear, visualizar, editar y eliminar pedidos
- Gestión de múltiples artículos por pedido
- Cálculo automático de totales brutos y con descuentos
- Conversión de moneda EUR a USD en tiempo real
- Persistencia de datos en archivo JSON
- Generación automática de IDs (formato O000, O001, etc.)
- Carga automática de pedidos al iniciar

## Requisitos

- **Java 17+** (JDK) - Descarga desde [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) o [OpenJDK](https://openjdk.java.net/)
- **Maven** - Instrucciones en [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
- **IDE recomendada:** Visual Studio Code, IntelliJ IDEA o Eclipse

## Configuración del Entorno

### Verificar Instalación

```bash
java -version  # Verificar Java 17+
mvn -version   # Verificar Maven
//(mvnd -version) # Verificar Maven con Daemon
```

### Extensiones IDE (Visual Studio Code)

- Java Extension Pack (de Microsoft)
- Maven for Java
- PlantUML (de jebbs)

## Compilación y Ejecución

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn exec:java
```

Alternativamente, ejecuta la clase `Main` directamente desde tu IDE.

## Manual de Usuario

### Ventana Principal

Al iniciar la aplicación, se muestra la ventana principal con:
- **Campo de búsqueda**: Para buscar pedidos por ID
- **Lista desplegable**: Para seleccionar pedidos desde la lista
- **Botones**: "Add New Order", "Edit Order", "Delete Order"
- **Área de visualización**: Muestra los detalles del pedido seleccionado

### 1. Visualizar un Pedido

**Opción A - Búsqueda por ID:**
1. Ingresa el ID del pedido (ej: O001) en el campo de texto (formato de id: Oxxx)
2. Haz clic en el botón "Search"
3. El pedido se mostrará en el área de visualización

**Opción B - Selección desde lista:**
1. Haz clic en el combo box "Orders List"
2. Selecciona el pedido deseado
3. El pedido se mostrará automáticamente

**Información mostrada:**
- ID del pedido
- Número de posiciones (artículos)
- Cantidad total de unidades
- Valor total bruto
- Valor total con descuento
- Detalles de cada artículo
- Conversión a USD (si la API está disponible)

### 2. Crear un Nuevo Pedido

1. Haz clic en el botón **"Add New Order"** en la ventana principal
2. Se abrirá la ventana "New Order"
3. Para cada artículo:
   - Ingresa el **nombre** del artículo
   - Ingresa la **cantidad** (número entero positivo)
   - Ingresa el **precio unitario** (número decimal)
   - Ingresa el **descuento** en porcentaje (0-100)
   - Haz clic en el botón **"+"** para agregar el artículo
4. Repite el paso 3 para agregar más artículos
5. Los totales se calculan automáticamente mientras agregas artículos
6. Haz clic en **"Save Order"** para guardar el pedido
7. El sistema generará automáticamente un ID (O000, O001, etc.)
8. La ventana se cerrará y el pedido aparecerá en la lista principal

**Validaciones:**
- Todos los campos son obligatorios
- La cantidad debe ser un número positivo
- El descuento debe estar entre 0 y 100%
- El pedido debe tener al menos un artículo

### 3. Editar un Pedido Existente

1. Primero, **visualiza el pedido** que deseas editar (usando búsqueda o selección)
2. Haz clic en el botón **"Edit Order"**
3. Confirma la edición en el diálogo de confirmación
4. Se abrirá la ventana "Edit Order" con una tabla de artículos
5. En la tabla, puedes editar:
   - **Cantidad**: Haz doble clic en la celda y modifica el valor
   - **Descuento (%)**: Haz doble clic en la celda y modifica el valor
6. **No se pueden editar**: Nombre y Precio Unitario (campos bloqueados)
7. Los totales se recalculan automáticamente al modificar valores
8. Haz clic en **"Save Order"** para guardar los cambios
9. Confirma el guardado en el diálogo
10. La ventana se cerrará y la vista principal se actualizará con el pedido modificado

**Validaciones:**
- La cantidad debe ser mayor que 0
- El descuento debe estar entre 0 y 100%

### 4. Eliminar un Pedido

1. **Selecciona o busca** el pedido que deseas eliminar
2. Haz clic en el botón **"Delete Order"**
3. Confirma la eliminación en el diálogo de confirmación
4. El pedido se eliminará de la lista y del archivo JSON
5. La lista de pedidos se actualizará automáticamente
6. El área de visualización se limpiará

### 5. Conversión de Moneda

La aplicación intenta convertir automáticamente el total del pedido de EUR a USD cuando visualizas un pedido. Si la API de conversión no está disponible:

- Se mostrará el pedido con el total en EUR
- Aparecerá un mensaje de advertencia indicando que no se pudo obtener el tipo de cambio
- La aplicación continuará funcionando normalmente

### Notas Importantes

- Todos los cambios se guardan automáticamente en el archivo `orders.json`
- Los pedidos se cargan automáticamente al iniciar la aplicación
- Los IDs se generan automáticamente en formato secuencial (O000, O001, O002...)
- Si cierras la ventana de creación/edición sin guardar, los cambios se perderán

## Estructura del Proyecto

```
Aleksei-Limin-M22-Intro-Ing-Software/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Main.java
│   │   │   ├── controller/
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── NewOrderController.java
│   │   │   │   └── EditOrderController.java
│   │   │   ├── model/
│   │   │   │   ├── Order.java
│   │   │   │   ├── Article.java
│   │   │   │   ├── Calculator.java
│   │   │   │   ├── Searcher.java
│   │   │   │   ├── ExchangeRate.java
│   │   │   │   └── OrderRepository.java
│   │   │   └── view/
│   │   │       ├── OrderView.java
│   │   │       ├── NewOrderView.java
│   │   │       └── EditOrderView.java
│   │   └── resources/
│   │       ├── orders.json
│   │       ├── app.png
│   │       ├── logback.xml
│   │       └── plantuml/
│   └── test/java/
├── pom.xml
└── README.md
```

## Arquitectura y Tecnologías

### Arquitectura MVC

El proyecto sigue el patrón **Modelo-Vista-Controlador**:

- **Modelo**: `Order`, `Article`, `OrderRepository`, `Calculator`, `Searcher`, `ExchangeRate`
- **Vista**: `OrderView`, `NewOrderView`, `EditOrderView`
- **Controlador**: `OrderController`, `NewOrderController`, `EditOrderController`

### Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación
- **Maven**: Gestión de dependencias y construcción
- **Swing (JFrame)**: Interfaz gráfica de usuario
- **Jackson Databind 2.17.0**: Serialización/deserialización JSON
- **SLF4J + Logback**: Sistema de logging
- **JUnit Jupiter 5.13.0**: Pruebas unitarias

### Persistencia de Datos

Los pedidos se almacenan en `src/main/resources/orders.json` en formato JSON. El archivo se actualiza automáticamente al crear, modificar o eliminar pedidos.

**Formato JSON:**
```json
[
  {
    "id": "O000",
    "articles": [
      {
        "name": "Artículo 1",
        "quantity": 2,
        "unitPrice": 10.5,
        "discount": 5.0
      }
    ]
  }
]
```

### API de Conversión de Moneda

La aplicación utiliza la API `https://api.exchangerate-api.com/v4/latest/EUR` para obtener el tipo de cambio EUR/USD en tiempo real. La implementación se encuentra en `ExchangeRate.java` y utiliza Java 11 HttpClient para realizar peticiones HTTP GET.

**Comportamiento:**
- Si la API está disponible: muestra el total en EUR y USD
- Si la API no está disponible: muestra solo el total en EUR con una advertencia
- La aplicación continúa funcionando normalmente en ambos casos

### Logging

El sistema utiliza SLF4J con Logback (`logback.xml`) para registrar:
- Carga y guardado de pedidos
- Advertencias de API de conversión
- Errores de I/O
- Información de depuración

## Contribución

1. Haz un fork del repositorio
2. Crea una nueva rama (`git checkout -b feature-nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -am 'Agregué nueva funcionalidad'`)
4. Empuja los cambios (`git push origin feature-nueva-funcionalidad`)
5. Crea un pull request

## Licencia

Este proyecto está bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.
