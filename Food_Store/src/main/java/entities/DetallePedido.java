package entities;

import java.util.ArrayList;

public class DetallePedido extends Base{
    private int cantidad;
    private double subtotal;
    private ArrayList<Producto> productos = new ArrayList<>();
}
