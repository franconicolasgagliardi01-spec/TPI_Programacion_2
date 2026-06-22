package service;

import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private List<Pedido> pedidos = new ArrayList<>();
    private Long nextId = 1L;
    // Contador independiente para los DetallePedido: cada detalle también
    // necesita su propio id único (dentro de su tipo), igual que el resto
    // de las entidades, ya que se persiste en su propia tabla.
    private Long nextDetalleId = 1L;
    private UsuarioService usuarioService;
    private ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // ---- CREATE (HU-PED-02) ----
    // El usuario debe existir. Los detalles se cargan después con
    // agregarDetalle(...), que delega en Pedido.addDetallePedido(...).
    public Pedido crear(Long idUsuario, FormaPago formaPago) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario); // valida que exista

        Pedido pedido = new Pedido(LocalDate.now(), Estado.PENDIENTE, formaPago, usuario);
        pedido.setId(nextId++);
        pedidos.add(pedido);
        return pedido;
    }

    // Agrega un detalle a un pedido ya creado, validando el producto y
    // delegando en el método obligatorio addDetallePedido de la entidad Pedido.
    public void agregarDetalle(Long idPedido, Long idProducto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        Pedido pedido = buscarPorId(idPedido);
        Producto producto = productoService.buscarPorId(idProducto); // valida que exista

        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);

        // addDetallePedido agrega el detalle al final de la lista; le asignamos
        // su id acá, ya que Pedido no conoce el contador de detalles.
        DetallePedido ultimoDetalle = pedido.getDetalles().get(pedido.getDetalles().size() - 1);
        ultimoDetalle.setId(nextDetalleId++);

        pedido.calcularTotal(); // usa la interfaz Calculable (void: setea el total internamente)
    }

    // ---- READ (HU-PED-01) ----
    public List<Pedido> listar() {
        return pedidos.stream()
                .filter(p -> !p.isEliminado())
                .toList();
    }

    public List<Pedido> listarPorUsuario(Long idUsuario) {
        return pedidos.stream()
                .filter(p -> !p.isEliminado() && p.getUsuario().getId().equals(idUsuario))
                .toList();
    }

    public Pedido buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id) && !p.isEliminado())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
    }

    // ---- UPDATE (HU-PED-03) ----
    public void actualizarEstado(Long id, Estado nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
    }

    public void actualizarFormaPago(Long id, FormaPago nuevaFormaPago) {
        Pedido pedido = buscarPorId(id);
        pedido.setFormaPago(nuevaFormaPago);
    }

    // ---- DELETE lógico (HU-PED-04) ----
    // Marca eliminado en el pedido y, en cascada, en sus detalles.
    public void eliminar(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
        pedido.getDetalles().forEach(d -> d.setEliminado(true));
    }

    public void agregarDesdeBD(Pedido pedido) {
        pedidos.add(pedido);
        if (pedido.getId() >= nextId) {
            nextId = pedido.getId() + 1;
        }
    }

    // Llamado por el DataLoader después de cargar los detalles de un pedido,
    // para que el contador de ids de detalle no choque con los ya guardados.
    public void sincronizarNextDetalleId(Long idDetalle) {
        if (idDetalle >= nextDetalleId) {
            nextDetalleId = idDetalle + 1;
        }
    }

    public List<Pedido> obtenerTodos() {
        return pedidos;
    }
}
