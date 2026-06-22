package entities;

import java.time.LocalDateTime;

public class DetallePedido extends Base {
    private int cantidad;
    private double subtotal;
    private Producto producto;

    public DetallePedido() {
    }

    // Constructor para CREAR un detalle nuevo (lo arma Pedido.addDetallePedido)
    public DetallePedido(int cantidad, double subtotal, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    // Constructor para RECONSTRUIR un detalle leído desde la BD (usado por el DataLoader)
    public DetallePedido(Long id, int cantidad, double subtotal, Producto producto,
                          boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a 0");
        } else {
            this.cantidad = cantidad;
        }
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
