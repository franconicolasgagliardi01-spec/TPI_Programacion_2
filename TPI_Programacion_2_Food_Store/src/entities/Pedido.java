package entities;

import enums.Estado;
import enums.FormaPago;

import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private ArrayList<DetallePedido> detallePedidos;

    public Pedido(LocalDate fecha, Estado estado, double total, FormaPago formaPago) {
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        detallePedidos = new ArrayList<>();
    }

    public void calcularTotal(){

    }

    public void addDetallePedido(){

    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto){
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto){

    }
}
