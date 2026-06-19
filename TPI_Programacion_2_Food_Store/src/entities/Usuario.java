package entities;

import enums.Rol;

import java.util.ArrayList;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private Rol rol;
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public Usuario(String nombre, String apellido, String mail, String celular, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.rol = rol;
    }
}
