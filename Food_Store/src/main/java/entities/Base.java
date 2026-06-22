package entities;

import java.time.LocalDateTime;

public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    // Constructor para objetos NUEVOS (creados en memoria por el usuario).
    // Todavía no tienen id (lo asigna el Service al crear), arrancan
    // como no eliminados y con la fecha de creación actual.
    public Base() {
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para objetos RECONSTRUIDOS desde la base de datos
    // (lo usa el DataLoader, que ya conoce el id, eliminado y createdAt reales).
    public Base(Long id, boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    // El Service/Saver lo usa para asignar el id generado en memoria o leído de la BD.
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    // Lo usa la baja lógica para marcar eliminado = true.
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
