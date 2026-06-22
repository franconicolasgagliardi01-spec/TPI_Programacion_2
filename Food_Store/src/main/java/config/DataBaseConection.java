package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConection {

    // Método centralizado: todo el que necesite una Connection pasa por acá,
    // así el PRAGMA foreign_keys siempre queda activado.
    public static Connection conectar(String url) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void crearBD(String url) {
        System.out.println("Creando BD...");
        try (Connection conn = conectar(url); Statement stmt = conn.createStatement()) { //Usamos try para la conexion para que se cierre automaticamente
            conn.setAutoCommit(false); //Le decimos que no guarde los datos hasta que le digamos
            //Creacion de las tablas
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS categorias (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE,
                descripcion TEXT,
                eliminado INTEGER NOT NULL DEFAULT 0 CHECK (eliminado IN (0, 1)),
                created_at TEXT NOT NULL DEFAULT (datetime('now'))
            );
        """);
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL CHECK (precio >= 0),
                descripcion  TEXT,
                stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
                imagen TEXT,
                disponible INTEGER NOT NULL DEFAULT 1 CHECK (disponible IN (0, 1)),
                id_categoria INTEGER NOT NULL,
                eliminado INTEGER NOT NULL DEFAULT 0 CHECK (eliminado IN (0, 1)),
                created_at TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (id_categoria) REFERENCES categorias(id)
                    ON DELETE RESTRICT
                    ON UPDATE CASCADE
            );
        """);
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                celular TEXT,
                rol TEXT NOT NULL CHECK (rol IN ('ADMIN', 'USUARIO')),
                eliminado INTEGER NOT NULL DEFAULT 0 CHECK (eliminado IN (0, 1)),
                created_at TEXT NOT NULL DEFAULT (datetime('now'))
                    );
        """);
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS pedidos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha TEXT NOT NULL DEFAULT (date('now')),
                estado TEXT NOT NULL DEFAULT 'PENDIENTE'
                CHECK (estado IN ('PENDIENTE', 'CONFIRMADO', 'TERMINADO', 'CANCELADO')),
                total REAL NOT NULL DEFAULT 0 CHECK (total >= 0),
                forma_pago TEXT CHECK (forma_pago IN ('TARJETA', 'TRANSFERENCIA', 'EFECTIVO')),
                id_usuario INTEGER NOT NULL,
                eliminado INTEGER NOT NULL DEFAULT 0 CHECK (eliminado IN (0, 1)),
                created_at TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
                    ON DELETE RESTRICT
                    ON UPDATE CASCADE
            );                       
        """);
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS detalle_pedidos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cantidad INTEGER NOT NULL CHECK (cantidad > 0),
                subtotal REAL NOT NULL CHECK (subtotal >= 0),
                id_pedido INTEGER NOT NULL,
                id_producto INTEGER NOT NULL,
                eliminado INTEGER NOT NULL DEFAULT 0 CHECK (eliminado IN (0, 1)),
                created_at TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (id_pedido) REFERENCES pedidos(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,
                FOREIGN KEY (id_producto) REFERENCES productos(id)
                    ON DELETE RESTRICT
                    ON UPDATE CASCADE
            );
        """);
            conn.commit();
            System.out.println("BD creada correctamente");
        } catch (SQLException e) {
            System.out.println("Error creando BD: " + e.getMessage());
        }
    }
}
