package entities;

import java.time.LocalDateTime;

public class Producto extends Base {
    private String nombre;
    private double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto() {
    }

    // Constructor para CREAR un producto nuevo desde el menú
    public Producto(String nombre, double precio, String descripcion, int stock, String imagen,
                     boolean disponible, Categoria categoria) {
        super();
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    // Constructor para RECONSTRUIR un producto leído desde la BD (usado por el DataLoader)
    public Producto(Long id, String nombre, double precio, String descripcion, int stock, String imagen,
                     boolean disponible, Categoria categoria, boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        } else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            //Aseguro que el nombre solo contenga letras
            System.out.println("Ingrese un nombre válido");
        } else {
            this.nombre = nombre.trim();
        }

    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            System.out.println("Ingrese un rango de precio correcto");
        } else {
            this.precio = precio;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("Ingrese una cantidad de stock correcta");
        } else {
            this.stock = stock;
        }
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
