package integrado.prog2.entities;
import java.util.Objects;
import java.time.LocalDateTime;

public abstract class Base {
    private static long ultimoId = 0;
    
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;
    protected Base() {
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    // No implemento un SetId, ya que se maneja autogenerado y setearlo manualmente puede dar lugar a IDs repetidos, por ello no expongo un setter.
    public void asignarId() {
        if (this.id == null) {
            this.id = ++ultimoId;
        }
    }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { 
        if (createdAt == null) throw new IllegalArgumentException("La fecha de creacion no puede ser nula");     
        this.createdAt = createdAt; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;
        Base base = (Base) o;
        return Objects.equals(id, base.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public abstract String toString();
}