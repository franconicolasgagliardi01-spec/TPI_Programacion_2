# Food Store – Sistema de Gestión de Pedidos de Comida

Trabajo Práctico Integrador para la materia **Programación 2** de la Tecnicatura Universitaria en Programación (A Distancia). Desarrollado en **Java 21** aplicando Programación Orientada a Objetos (POO), colecciones en memoria y arquitectura en capas.

---

## 👥 Integrantes
* **Franco Gagliardi**
* **Ciro Cattaneo**
* **Ramiro Quiroga**

---

## 🚀 Características del Proyecto
* **Interfaz de Consola:** Menú interactivo y guiado con validaciones robustas de entrada de datos.
* **Paradigma POO Avanzado:** Implementación de herencia (clase abstracta `Base`), composición (Pedido-DetallePedido), encapsulamiento e interfaces.
* **Persistencia Temporaria:** Gestión integral de datos en memoria mediante el uso estratégico de **Colecciones**.
* **Arquitectura Limpia:** Separación estricta de responsabilidades en paquetes (`entities`, `enums`, `exception`, `config`).
* **Bajas Lógicas (Soft Delete):** Ningún dato histórico es borrado físicamente; se utiliza la bandera `eliminado = true` para preservar la auditoría.

---

## 📂 Estructura de Paquetes (`src/integrado/prog2/`)

```text
src/
└── integrado/
    └── prog2/
        ├── Main.java                 # Inicialización y ejecución del menú principal
        ├── config/                   # Conexión técnica e infraestructura
        ├── entities/                 # Clases del modelo UML (Dominio e Interfaces)
        ├── enums/                    # Enumeraciones (Rol, Estado, FormaPago)
        └── exception/                # Excepciones personalizadas del negocio