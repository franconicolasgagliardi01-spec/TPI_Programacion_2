package service;

import entities.Categoria;
import entities.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private List<Producto> productos = new ArrayList<>();
    private Long nextId = 1L;
    private CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ---- CREATE (HU-PROD-02) ----
    public Producto crear(String nombre, double precio, String descripcion,
                           int stock, String imagen, boolean disponible, Long idCategoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        // Valida que la categoría exista y no esté eliminada (lanza excepción si no).
        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        producto.setId(nextId++);
        productos.add(producto);
        return producto;
    }

    // ---- READ (HU-PROD-01) ----
    public List<Producto> listar() {
        return productos.stream()
                .filter(p -> !p.isEliminado())
                .toList();
    }

    public List<Producto> listarPorCategoria(Long idCategoria) {
        return productos.stream()
                .filter(p -> !p.isEliminado() && p.getCategoria().getId().equals(idCategoria))
                .toList();
    }

    public Producto buscarPorId(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id) && !p.isEliminado())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
    }

    // ---- UPDATE (HU-PROD-03) ----
    public void editar(Long id, String nuevoNombre, Double nuevoPrecio, String nuevaDescripcion,
                        Integer nuevoStock, String nuevaImagen, Boolean nuevaDisponibilidad,
                        Long nuevaIdCategoria) {
        Producto producto = buscarPorId(id);

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            producto.setNombre(nuevoNombre);
        }
        if (nuevoPrecio != null) {
            if (nuevoPrecio < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            producto.setPrecio(nuevoPrecio);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            producto.setDescripcion(nuevaDescripcion);
        }
        if (nuevoStock != null) {
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }
            producto.setStock(nuevoStock);
        }
        if (nuevaImagen != null && !nuevaImagen.isBlank()) {
            producto.setImagen(nuevaImagen);
        }
        if (nuevaDisponibilidad != null) {
            producto.setDisponible(nuevaDisponibilidad);
        }
        if (nuevaIdCategoria != null) {
            Categoria categoria = categoriaService.buscarPorId(nuevaIdCategoria);
            producto.setCategoria(categoria);
        }
    }

    // ---- DELETE lógico (HU-PROD-04) ----
    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
    }

    public void agregarDesdeBD(Producto producto) {
        productos.add(producto);
        if (producto.getId() >= nextId) {
            nextId = producto.getId() + 1;
        }
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }
}
