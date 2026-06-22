package entities;

import enums.Rol;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String email;
    private String celular;
    private Rol rol;
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public Usuario() {
    }

    // Constructor para CREAR un usuario nuevo desde el menú
    public Usuario(String nombre, String apellido, String email, String celular, Rol rol) {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setEmail(email);
        setCelular(celular);
        setRol(rol);
    }

    // Constructor para RECONSTRUIR un usuario leído desde la BD (usado por el DataLoader)
    public Usuario(Long id, String nombre, String apellido, String email, String celular, Rol rol,
                   boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.celular = celular;
        this.rol = rol;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        } else if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            //Aseguro que el nombre solo contenga letras
            System.out.println("Ingrese un apellido válido");
        } else {
            this.apellido = apellido.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        } else {
            this.email = email;
        }

    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            System.out.println("Este campo es obligatorio, no puede estar vacío");
        }  else if (!celular.matches("[0-9]+")) {
            System.out.println("Error, el celular solo puede contener números");
        } else {
            this.celular = celular;
        }

    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        if (rol == null) {
            System.out.println("Este campo es obligatorio, ingrese un rol válido");
        } else {
            this.rol = rol;
        }
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
}
