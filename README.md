# Proyecto Java Maven - Sistema de Gestión de Pedidos

Este proyecto es una aplicación de gestión de pedidos desarrollada en Java utilizando Maven, Swing (JFrame) y arquitectura MVC (Modelo-Vista-Controlador). La aplicación permite buscar pedidos, calcular totales con descuentos y realizar conversión de moneda EUR a USD utilizando una API externa.

## Tabla de Contenidos

- [Introducción](#introducción)
- [Requisitos](#requisitos)
- [Configuración del Entorno](#configuración-del-entorno)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Compilación y Ejecución](#compilación-y-ejecución)
- [Integración de API de Conversión de Moneda](#integración-de-api-de-conversión-de-moneda)
- [Contribución](#contribución)
- [Licencia](#licencia)

---

## Introducción

Este es un proyecto de sistema de gestión de pedidos desarrollado en **Java** utilizando **Maven** para la gestión de dependencias. El proyecto implementa una aplicación de escritorio con interfaz gráfica Swing que permite cargar, buscar y visualizar pedidos con sus artículos y cálculos de totales. Además, incluye funcionalidad de conversión de moneda EUR a USD en tiempo real.

## Requisitos

Antes de comenzar, asegúrate de tener instalado lo siguiente en tu sistema:

- **Java 17+** (JDK) - El proyecto está configurado para Java 17. Puedes descargarlo desde [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) o [OpenJDK](https://openjdk.java.net/)
- **Maven** - Puedes instalar Maven siguiendo las instrucciones en su sitio oficial: [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
- **IDE recomendada:** Visual Studio Code o cualquier editor de tu preferencia con soporte para Java.

## Configuración del Entorno

### 1. Instalar Java
Asegúrate de que tienes **Java 17** o una versión superior instalada. Puedes verificarlo con el siguiente comando:

```bash
java -version
```

### 2. Instalar Maven
Una vez que tengas Java instalado, puedes proceder con la instalación de Maven. Para verificar que Maven esté instalado correctamente, ejecuta el siguiente comando:

```bash
mvn -version
```

### 3. Configurar el IDE
Para trabajar con este proyecto, puedes usar cualquier IDE que soporte Java, como Visual Studio Code, IntelliJ IDEA, Eclipse, etc. Si estás usando Visual Studio Code, asegúrate de instalar las siguientes extensiones:

- Java Extension Pack (de Microsoft)
- Maven for Java

## Estructura del proyecto

```plaintext
Aleksei-Limin-M22-Intro-Ing-Software/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── Main.java
│   │   │           ├── controller/
│   │   │           │   └── OrderController.java
│   │   │           ├── model/
│   │   │           │   ├── Order.java
│   │   │           │   ├── Article.java
│   │   │           │   ├── Calculator.java
│   │   │           │   ├── Searcher.java
│   │   │           │   └── ExchangeRate.java
│   │   │           └── view/
│   │   │               └── OrderView.java
│   │   └── resources/
│   │       ├── orders.json
│   │       ├── app.png
│   │       └── logback.xml
│   └── test/
│       └── java/
├── pom.xml
├── target/
└── README.md
```

## Compilación y ejecución

Para compilar y ejecutar el proyecto, sigue estos pasos:

Abre una terminal en la raíz del proyecto.

Ejecuta el siguiente comando para compilar el proyecto:
```bash
mvn clean install
```
Para ejecutar la aplicación, usa el siguiente comando:
```bash
mvn exec:java
```

## Integración de API de Conversión de Moneda

El proyecto incluye funcionalidad de conversión de moneda EUR a USD utilizando una API externa.

### API Endpoint Utilizado

La aplicación utiliza el siguiente endpoint para obtener el tipo de cambio en tiempo real:

```
https://api.exchangerate.host/latest?base=EUR&symbols=USD
```

### Ubicación y Funcionalidad

La clase `ExchangeRate` se encuentra en `src/main/java/com/example/model/ExchangeRate.java` y es responsable de:

- Realizar peticiones HTTP GET a la API de tipos de cambio utilizando Java 11 HttpClient
- Parsear manualmente la respuesta JSON (sin usar bibliotecas externas)
- Extraer el valor del tipo de cambio USD
- Retornar el tipo de cambio como `BigDecimal` para precisión en cálculos monetarios

**Método principal**: `public static BigDecimal getCurrentEurUsdRate() throws Exception`

### Comportamiento cuando la API no está disponible

Si la API no está disponible o falla la petición (por ejemplo, problemas de red, timeout, o respuesta inválida), la aplicación:

- Captura la excepción y registra una advertencia en los logs
- Muestra el pedido con el total en EUR
- Muestra un mensaje de advertencia indicando que no se pudo obtener el tipo de cambio
- La aplicación continúa funcionando normalmente sin la conversión a USD

### Modificaciones Realizadas

**OrderController**: Después de encontrar un pedido, calcula el total en EUR usando `getDiscountedTotal()`, llama a `ExchangeRate.getCurrentEurUsdRate()` para obtener el tipo de cambio, calcula el total en USD, y muestra los resultados. En caso de error de la API, muestra una advertencia.

**OrderView**: Se han añadido dos nuevos métodos:
- `showOrder(Order order, BigDecimal totalEur, BigDecimal rate, BigDecimal totalUsd)`: Muestra el pedido con la conversión a USD
- `showOrderWithoutUsd(Order order, BigDecimal totalEur, String warning)`: Muestra el pedido con advertencia cuando la API no está disponible

## Contribución

Si deseas contribuir a este proyecto, por favor sigue estos pasos:
- Haz un fork del repositorio.
- Crea una nueva rama (git checkout -b feature-nueva-funcionalidad).
- Realiza tus cambios y haz commit (git commit -am 'Agregué nueva funcionalidad').
- Empuja los cambios a tu fork (git push origin feature-nueva-funcionalidad).
- Crea un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.
