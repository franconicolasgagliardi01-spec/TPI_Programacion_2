package entities;

import java.time.LocalDateTime;

public class Base {
    private Long id;
    private boolean eliminado = false;
    private LocalDateTime createdAt;

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
