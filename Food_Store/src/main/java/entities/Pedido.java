package entities;

import enums.Estado;
import enums.FormaPago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private ArrayList<DetallePedido> detallePedidos = new ArrayList<>();

    public Pedido() {
    }

    // Constructor para CREAR un pedido nuevo desde el menú
    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0;
    }

    // Constructor para RECONSTRUIR un pedido leído desde la BD (usado por el DataLoader).
    // Los detalles se agregan después, uno por uno, con agregarDetalleDesdeBD(...).
    public Pedido(Long id, LocalDate fecha, Estado estado, double total, FormaPago formaPago,
                  Usuario usuario, boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        this.usuario = usuario;
    }

    // ---- Método obligatorio de la consigna: agrega un detalle al pedido ----
    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        double subtotal = cantidad * precioUnitario;
        DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
        detallePedidos.add(detalle);
    }

    // Usado por el DataLoader: agrega un detalle ya reconstruido (con su id real de la BD)
    public void agregarDetalleDesdeBD(DetallePedido detalle) {
        detallePedidos.add(detalle);
    }

    // ---- Método obligatorio de la consigna: busca un detalle por producto ----
    public Optional<DetallePedido> findeDetallePedidoByProducto(Producto producto) {
        return detallePedidos.stream()
                .filter(d -> d.getProducto().getId().equals(producto.getId()))
                .findFirst();
    }

    // ---- Método obligatorio de la consigna: elimina un detalle por producto ----
    public void deleteDetallePedidoByProducto(Producto producto) {
        findeDetallePedidoByProducto(producto)
                .ifPresent(detalle -> detallePedidos.remove(detalle));
    }

    // ---- Implementación de Calculable: suma los subtotales de los detalles ----
    @Override
    public void calcularTotal() {
        this.total = detallePedidos.stream()
                .filter(d -> !d.isEliminado())
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detallePedidos;
    }
}
