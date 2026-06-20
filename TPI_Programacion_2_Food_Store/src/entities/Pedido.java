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

    public void calcularTotal(){

    }

}
