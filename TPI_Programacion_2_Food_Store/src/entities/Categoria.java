package entities;
import java.util.ArrayList;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;
    private ArrayList<Producto> productos = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        } else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
            //Aseguro que el nombre solo contenga letras
            System.out.println("Ingrese un nombre válido");
        } else {
            this.nombre = nombre.trim();
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        } else {
            this.descripcion = descripcion;
        }
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

}
