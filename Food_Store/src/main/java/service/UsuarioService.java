package service;

import entities.Usuario;
import enums.Rol;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private List<Usuario> usuarios = new ArrayList<>();
    private Long nextId = 1L;

    // ---- CREATE (HU-USR-02) ----
    public Usuario crear(String nombre, String apellido, String email, String celular, Rol rol) {
        // FIX: validaciones de formato antes de instanciar el objeto,
        // así un campo inválido impide la creación (antes el setter rechazaba
        // silenciosamente y el objeto quedaba con campos null).
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El apellido solo puede contener letras");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (celular == null || celular.isBlank()) {
            throw new IllegalArgumentException("El celular no puede estar vacío");
        }
        if (!celular.matches("[0-9]+")) {
            throw new IllegalArgumentException("El celular solo puede contener números");
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
            if (!nuevoNombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                throw new IllegalArgumentException("El nombre solo puede contener letras");
            }
            usuario.setNombre(nuevoNombre);
        }
        if (nuevoApellido != null && !nuevoApellido.isBlank()) {
            if (!nuevoApellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                throw new IllegalArgumentException("El apellido solo puede contener letras");
            }
            usuario.setApellido(nuevoApellido);
        }
        if (nuevoEmail != null && !nuevoEmail.isBlank()) {
            validarMailUnico(nuevoEmail, id);
            usuario.setEmail(nuevoEmail);
        }
        if (nuevoCelular != null && !nuevoCelular.isBlank()) {
            if (!nuevoCelular.matches("[0-9]+")) {
                throw new IllegalArgumentException("El celular solo puede contener números");
            }
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

    private void validarMailUnico(String email, Long idAIgnorar) {
        boolean existe = usuarios.stream()
                .anyMatch(u -> !u.isEliminado()
                        && u.getEmail().equalsIgnoreCase(email)
                        && (idAIgnorar == null || !u.getId().equals(idAIgnorar)));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
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
