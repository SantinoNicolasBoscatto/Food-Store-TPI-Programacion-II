package integrado.prog2.services.Crud_Generico;
import integrado.prog2.entities.Base;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudGenerico<T extends Base> implements CrudService<T> {
    protected List<T> datos = new ArrayList<>();
    
    @Override
    public void crear(T entidad) {
        validarCreacion(entidad);
        entidad.asignarId(); 
        datos.add(entidad);
    }

    @Override
    public T buscarPorId(Long id) {
        return datos.stream()
                .filter(e -> e.getId().equals(id) && !e.isEliminado())
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontradaException("La entidad de Id " + id + " no fue encontrada"));
    }

    @Override
    public List<T> listarTodos() {
        return datos.stream()
                .filter(e -> !e.isEliminado())
                .toList();
    }

    @Override
    public void actualizar(T entidad) {
        T existente = buscarPorId(entidad.getId());
        validarActualizacion(existente, entidad);
        aplicarCambios(existente, entidad);
    }

    @Override
    public void eliminar(Long id) {
        T entidad = buscarPorId(id);
        entidad.setEliminado(true);
    }

    protected abstract void validarCreacion(T entidad);
    protected void validarActualizacion(T existente, T nuevaInfo) {}
    protected abstract void aplicarCambios(T existente, T nuevaInfo);
}