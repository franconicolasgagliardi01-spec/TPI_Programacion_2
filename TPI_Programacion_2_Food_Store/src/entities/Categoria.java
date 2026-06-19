package entities;

import java.util.ArrayList;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;
    private ArrayList<Producto> productos = new ArrayList<>();
}
