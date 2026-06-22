package service;

import entities.Usuario;
import enums.Rol;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private List<Usuario> usuarios = new ArrayList<>();
    private Long nextId = 1L;

    // ---- CREATE (HU-USR-02) ----
    public Usuario crear(String nombre, String apellido, String email,
                          String celular, Rol rol) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El mail no puede estar vacío");
        }
        validarMailUnico(email, null);

        Usuario usuario = new Usuario(nombre, apellido, email, celular, rol);
        usuario.setId(nextId++);
        usuarios.add(usuario);
        return usuario;
    }

    // ---- READ (HU-USR-01) ----
    public List<Usuario> listar() {
        return usuarios.stream()
                .filter(u -> !u.isEliminado())
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id) && !u.isEliminado())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    // ---- UPDATE (HU-USR-03) ----
    public void editar(Long id, String nuevoNombre, String nuevoApellido, String nuevoEmail,
                        String nuevoCelular, Rol nuevoRol) {
        Usuario usuario = buscarPorId(id);

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            usuario.setNombre(nuevoNombre);
        }
        if (nuevoApellido != null && !nuevoApellido.isBlank()) {
            usuario.setApellido(nuevoApellido);
        }
        if (nuevoEmail != null && !nuevoEmail.isBlank()) {
            validarMailUnico(nuevoEmail, id);
            usuario.setEmail(nuevoEmail);
        }
        if (nuevoCelular != null && !nuevoCelular.isBlank()) {
            usuario.setCelular(nuevoCelular);
        }
        if (nuevoRol != null) {
            usuario.setRol(nuevoRol);
        }
    }

    // ---- DELETE lógico (HU-USR-04) ----
    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true);
    }

    // Recorre la colección para validar mail único, ignorando al propio
    // usuario cuando se está editando (idAIgnorar puede ser null al crear).
    private void validarMailUnico(String email, Long idAIgnorar) {
        boolean existe = usuarios.stream()
                .anyMatch(u -> !u.isEliminado()
                        && u.getEmail().equalsIgnoreCase(email)
                        && (idAIgnorar == null || !u.getId().equals(idAIgnorar)));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un usuario con ese mail");
        }
    }

    public void agregarDesdeBD(Usuario usuario) {
        usuarios.add(usuario);
        if (usuario.getId() >= nextId) {
            nextId = usuario.getId() + 1;
        }
    }

    public List<Usuario> obtenerTodos() {
        return usuarios;
    }
}
