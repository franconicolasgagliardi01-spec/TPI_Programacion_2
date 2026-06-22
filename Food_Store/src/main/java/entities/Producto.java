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

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        // FIX: productos pueden tener números y caracteres especiales en el nombre
        // (ej: "Coca-Cola 500ml", "Pan x12"), solo se rechaza vacío.
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío");
        } else {
            this.nombre = nombre.trim();
        }
    }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) {
        if (precio < 0) {
            System.out.println("El precio no puede ser negativo");
        } else {
            this.precio = precio;
        }
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            System.out.println("La descripción no puede estar vacía");
        } else {
            this.descripcion = descripcion;
        }
    }

    public int getStock() { return stock; }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("El stock no puede ser negativo");
        } else {
            this.stock = stock;
        }
    }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
