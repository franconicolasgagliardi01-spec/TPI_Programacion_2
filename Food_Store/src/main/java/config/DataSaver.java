package config;

import entities.Categoria;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSaver {

    // Persiste todo lo que hay en memoria a SQLite. Borra el contenido de las
    // tablas y vuelve a insertar todo con el id que cada objeto ya tiene en
    // memoria (el id NO lo genera SQLite, ya fue asignado por cada Service).
    public static void guardarTodo(String url, CategoriaService categoriaService,
                                    ProductoService productoService, UsuarioService usuarioService,
                                    PedidoService pedidoService) {
        try (Connection conn = DataBaseConection.conectar(url)) {
            conn.setAutoCommit(false);

            limpiarTablas(conn);

            guardarCategorias(conn, categoriaService);
            guardarProductos(conn, productoService);
            guardarUsuarios(conn, usuarioService);
            guardarPedidos(conn, pedidoService);
            guardarDetallePedidos(conn, pedidoService);

            conn.commit();
            System.out.println("Datos guardados correctamente en la BD.");
        } catch (SQLException e) {
            System.out.println("Error al guardar datos en la BD: " + e.getMessage());
        }
    }

    // Se borra en orden inverso a las dependencias para no romper foreign keys.
    private static void limpiarTablas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM detalle_pedidos");
            stmt.execute("DELETE FROM pedidos");
            stmt.execute("DELETE FROM usuarios");
            stmt.execute("DELETE FROM productos");
            stmt.execute("DELETE FROM categorias");
        }
    }

    private static void guardarCategorias(Connection conn, CategoriaService categoriaService) throws SQLException {
        String sql = "INSERT INTO categorias (id, nombre, descripcion, eliminado, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Categoria c : categoriaService.obtenerTodas()) {
                ps.setLong(1, c.getId());
                ps.setString(2, c.getNombre());
                ps.setString(3, c.getDescripcion());
                ps.setInt(4, c.isEliminado() ? 1 : 0);
                ps.setString(5, c.getCreatedAt().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void guardarProductos(Connection conn, ProductoService productoService) throws SQLException {
        String sql = "INSERT INTO productos (id, nombre, precio, descripcion, stock, imagen, disponible, " +
                "id_categoria, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Producto p : productoService.obtenerTodos()) {
                ps.setLong(1, p.getId());
                ps.setString(2, p.getNombre());
                ps.setDouble(3, p.getPrecio());
                ps.setString(4, p.getDescripcion());
                ps.setInt(5, p.getStock());
                ps.setString(6, p.getImagen());
                ps.setInt(7, p.isDisponible() ? 1 : 0);
                ps.setLong(8, p.getCategoria().getId());
                ps.setInt(9, p.isEliminado() ? 1 : 0);
                ps.setString(10, p.getCreatedAt().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void guardarUsuarios(Connection conn, UsuarioService usuarioService) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nombre, apellido, email, celular, rol, eliminado, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Usuario u : usuarioService.obtenerTodos()) {
                ps.setLong(1, u.getId());
                ps.setString(2, u.getNombre());
                ps.setString(3, u.getApellido());
                ps.setString(4, u.getEmail());
                ps.setString(5, u.getCelular());
                ps.setString(6, u.getRol().name());
                ps.setInt(7, u.isEliminado() ? 1 : 0);
                ps.setString(8, u.getCreatedAt().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void guardarPedidos(Connection conn, PedidoService pedidoService) throws SQLException {
        String sql = "INSERT INTO pedidos (id, fecha, estado, total, forma_pago, id_usuario, eliminado, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Pedido p : pedidoService.obtenerTodos()) {
                ps.setLong(1, p.getId());
                ps.setString(2, p.getFecha().toString());
                ps.setString(3, p.getEstado().name());
                ps.setDouble(4, p.getTotal());
                if (p.getFormaPago() == null) {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(5, p.getFormaPago().name());
                }
                ps.setLong(6, p.getUsuario().getId());
                ps.setInt(7, p.isEliminado() ? 1 : 0);
                ps.setString(8, p.getCreatedAt().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void guardarDetallePedidos(Connection conn, PedidoService pedidoService) throws SQLException {
        String sql = "INSERT INTO detalle_pedidos (id, cantidad, subtotal, id_pedido, id_producto, eliminado, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Pedido pedido : pedidoService.obtenerTodos()) {
                for (DetallePedido d : pedido.getDetalles()) {
                    ps.setLong(1, d.getId());
                    ps.setInt(2, d.getCantidad());
                    ps.setDouble(3, d.getSubtotal());
                    ps.setLong(4, pedido.getId());
                    ps.setLong(5, d.getProducto().getId());
                    ps.setInt(6, d.isEliminado() ? 1 : 0);
                    ps.setString(7, d.getCreatedAt().toString());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }
}
