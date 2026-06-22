package config;

import entities.Categoria;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import enums.Rol;
import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DataLoader {

    // SQLite guarda los TEXT de fecha con espacio ("2026-06-20 14:30:00").
    // LocalDateTime.parse necesita formato ISO con "T", por eso se reemplaza.
    private static LocalDateTime parseFecha(String fecha) {
        return LocalDateTime.parse(fecha.replace(" ", "T"));
    }

    // Carga las 5 tablas en orden de dependencias y devuelve los Services ya poblados.
    public static void cargarTodo(String url, CategoriaService categoriaService,
                                   ProductoService productoService, UsuarioService usuarioService,
                                   PedidoService pedidoService) {
        try (Connection conn = DataBaseConection.conectar(url)) {

            Map<Long, Categoria> categoriasPorId = cargarCategorias(conn, categoriaService);
            Map<Long, Producto> productosPorId = cargarProductos(conn, productoService, categoriasPorId);
            Map<Long, Usuario> usuariosPorId = cargarUsuarios(conn, usuarioService);
            Map<Long, Pedido> pedidosPorId = cargarPedidos(conn, pedidoService, usuariosPorId);
            cargarDetallePedidos(conn, pedidosPorId, productosPorId, pedidoService);

            System.out.println("Datos cargados correctamente desde la BD.");
        } catch (SQLException e) {
            System.out.println("Error al cargar datos desde la BD: " + e.getMessage());
        }
    }

    private static Map<Long, Categoria> cargarCategorias(Connection conn, CategoriaService categoriaService)
            throws SQLException {
        Map<Long, Categoria> mapa = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM categorias")) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("eliminado") == 1,
                        parseFecha(rs.getString("created_at"))
                );
                categoriaService.agregarDesdeBD(categoria);
                mapa.put(categoria.getId(), categoria);
            }
        }
        return mapa;
    }

    private static Map<Long, Producto> cargarProductos(Connection conn, ProductoService productoService,
                                                         Map<Long, Categoria> categoriasPorId) throws SQLException {
        Map<Long, Producto> mapa = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                Categoria categoria = categoriasPorId.get(rs.getLong("id_categoria"));
                Producto producto = new Producto(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getString("imagen"),
                        rs.getInt("disponible") == 1,
                        categoria,
                        rs.getInt("eliminado") == 1,
                        parseFecha(rs.getString("created_at"))
                );
                productoService.agregarDesdeBD(producto);
                mapa.put(producto.getId(), producto);
                if (categoria != null) {
                    categoria.agregarProducto(producto);
                }
            }
        }
        return mapa;
    }

    private static Map<Long, Usuario> cargarUsuarios(Connection conn, UsuarioService usuarioService)
            throws SQLException {
        Map<Long, Usuario> mapa = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("celular"),
                        Rol.valueOf(rs.getString("rol")),
                        rs.getInt("eliminado") == 1,
                        parseFecha(rs.getString("created_at"))
                );
                usuarioService.agregarDesdeBD(usuario);
                mapa.put(usuario.getId(), usuario);
            }
        }
        return mapa;
    }

    private static Map<Long, Pedido> cargarPedidos(Connection conn, PedidoService pedidoService,
                                                     Map<Long, Usuario> usuariosPorId) throws SQLException {
        Map<Long, Pedido> mapa = new HashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pedidos")) {

            while (rs.next()) {
                Usuario usuario = usuariosPorId.get(rs.getLong("id_usuario"));
                String formaPagoStr = rs.getString("forma_pago");

                Pedido pedido = new Pedido(
                        rs.getLong("id"),
                        LocalDate.parse(rs.getString("fecha")),
                        Estado.valueOf(rs.getString("estado")),
                        rs.getDouble("total"),
                        formaPagoStr == null ? null : FormaPago.valueOf(formaPagoStr),
                        usuario,
                        rs.getInt("eliminado") == 1,
                        parseFecha(rs.getString("created_at"))
                );
                pedidoService.agregarDesdeBD(pedido);
                mapa.put(pedido.getId(), pedido);
                if (usuario != null) {
                    usuario.getPedidos().add(pedido);
                }
            }
        }
        return mapa;
    }

    private static void cargarDetallePedidos(Connection conn, Map<Long, Pedido> pedidosPorId,
                                              Map<Long, Producto> productosPorId,
                                              PedidoService pedidoService) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM detalle_pedidos")) {

            while (rs.next()) {
                Pedido pedido = pedidosPorId.get(rs.getLong("id_pedido"));
                Producto producto = productosPorId.get(rs.getLong("id_producto"));

                DetallePedido detalle = new DetallePedido(
                        rs.getLong("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal"),
                        producto,
                        rs.getInt("eliminado") == 1,
                        parseFecha(rs.getString("created_at"))
                );
                if (pedido != null) {
                    pedido.agregarDetalleDesdeBD(detalle);
                }
                pedidoService.sincronizarNextDetalleId(detalle.getId());
            }
        }
    }
}
