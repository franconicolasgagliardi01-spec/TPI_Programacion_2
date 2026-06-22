# Food Store – Sistema de Gestión de Pedidos de Comida

Trabajo Práctico Integrador para la materia **Programación 2** de la **Tecnicatura Universitaria en Programación (FRM)**.

Aplicación desarrollada en **Java 25** utilizando **Programación Orientada a Objetos**, persistencia de datos con **SQLite**, arquitectura en capas y gestión completa de pedidos de comida mediante consola.

---

## 👥 Integrantes

* **Franco Gagliardi**
* **Ciro Cattaneo**
* **Ramiro Quiroga**

---

## 📋 Descripción

Food Store es un sistema de gestión de pedidos que permite administrar:

* Categorías de productos
* Productos
* Usuarios
* Pedidos y sus detalles

Toda la información se almacena en una base de datos **SQLite**, manteniendo la integridad de los datos mediante claves foráneas y validaciones de negocio.

---

## 🚀 Funcionalidades

### Gestión de Categorías

* Alta de categorías
* Modificación de datos
* Búsqueda por ID
* Listado completo
* Baja lógica

### Gestión de Productos

* Alta de productos
* Asociación a categorías
* Control de stock
* Actualización de información
* Búsqueda por ID
* Listado completo
* Baja lógica

### Gestión de Usuarios

* Alta de usuarios
* Asignación de roles
* Modificación de datos
* Búsqueda por ID
* Listado completo
* Baja lógica

### Gestión de Pedidos

* Creación de pedidos
* Asociación a usuarios
* Agregado y eliminación de detalles
* Cálculo automático del total
* Gestión de estados
* Selección de forma de pago
* Listado y consulta de pedidos

---

## 🛠 Tecnologías Utilizadas

* **Java 25**
* **Maven**
* **SQLite**
* **JDBC (sqlite-jdbc)**
* **Programación Orientada a Objetos**
* **Colecciones Java**
* **Arquitectura en Capas**

---

## 🏗 Conceptos Aplicados

### Programación Orientada a Objetos

* Encapsulamiento
* Herencia
* Polimorfismo
* Composición
* Interfaces

### Patrones de Diseño y Organización

* Separación por capas
* Servicios de negocio
* Entidades de dominio
* Persistencia desacoplada

### Persistencia

* Base de datos SQLite
* Carga automática de datos al iniciar
* Guardado automático al finalizar
* Integridad referencial mediante claves foráneas

### Gestión de Datos

* Bajas lógicas mediante campo `eliminado`
* Trazabilidad mediante `created_at`
* Validaciones de negocio
* Control de consistencia de datos

---

## 📂 Estructura del Proyecto

```text
src/main/java/
│
├── config/
│   ├── DataBaseConection.java
│   ├── DataLoader.java
│   └── DataSaver.java
│
├── entities/
│   ├── Base.java
│   ├── Calculable.java
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java
│   └── DetallePedido.java
│
├── enums/
│   ├── Estado.java
│   ├── FormaPago.java
│   └── Rol.java
│
│
├── service/
│   ├── CategoriaService.java
│   ├── ProductoService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
│
└── Main.java
```

---

## 🗄 Modelo de Datos

El sistema trabaja con las siguientes entidades principales:

* **Categoría**
* **Producto**
* **Usuario**
* **Pedido**
* **DetallePedido**

Relaciones principales:

* Una categoría posee muchos productos.
* Un usuario puede realizar muchos pedidos.
* Un pedido contiene múltiples detalles.
* Cada detalle referencia un producto.

---

## ▶ Ejecución

### Requisitos

* JDK 25 o superior
* Maven 3.9+

### Compilar

```bash
mvn clean compile
```

### Ejecutar

```bash
mvn exec:java
```

o desde el IDE ejecutando la clase:

```java
Main
```

---

## 📚 Objetivos Académicos

Este proyecto fue desarrollado para aplicar los contenidos de la materia Programación 2:

* Programación Orientada a Objetos
* Colecciones
* Persistencia de datos
* Arquitectura de software
* Manejo de excepciones
* Modelado UML
* Buenas prácticas de programación
