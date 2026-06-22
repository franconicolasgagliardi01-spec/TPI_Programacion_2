package service;

import entities.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private List<Categoria> categorias = new ArrayList<>();
    private Long nextId = 1L;

    // ---- CREATE (HU-CAT-02) ----
    public Categoria crear(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        boolean existe = categorias.stream()
                .anyMatch(c -> !c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre));
        if (existe) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = new Categoria(nombre, descripcion);
        categoria.setId(nextId++);
        categorias.add(categoria);
        return categoria;
    }

    // ---- READ (HU-CAT-01) ----
    public List<Categoria> listar() {
        return categorias.stream()
                .filter(c -> !c.isEliminado())
                .toList();
    }

    public Categoria buscarPorId(Long id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id) && !c.isEliminado())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + id));
    }

    // ---- UPDATE (HU-CAT-03) ----
    public void editar(Long id, String nuevoNombre, String nuevaDescripcion) {
        Categoria categoria = buscarPorId(id);

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            boolean existe = categorias.stream()
                    .anyMatch(c -> !c.isEliminado()
                            && !c.getId().equals(id)
                            && c.getNombre().equalsIgnoreCase(nuevoNombre));
            if (existe) {
                throw new IllegalArgumentException("Ya existe otra categoría con ese nombre");
            }
            categoria.setNombre(nuevoNombre);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            categoria.setDescripcion(nuevaDescripcion);
        }
    }

    // ---- DELETE lógico (HU-CAT-04) ----
    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoria.setEliminado(true);
    }

    // Usado por el DataLoader al cargar desde la BD, y por otros Services
    // que necesiten reinsertar objetos ya existentes en memoria.
    public void agregarDesdeBD(Categoria categoria) {
        categorias.add(categoria);
        if (categoria.getId() >= nextId) {
            nextId = categoria.getId() + 1;
        }
    }

    // Usado por el DataSaver al persistir todo al cerrar el programa.
    public List<Categoria> obtenerTodas() {
        return categorias;
    }
}
