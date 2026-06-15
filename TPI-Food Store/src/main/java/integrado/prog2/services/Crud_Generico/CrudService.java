package integrado.prog2.services.Crud_Generico;
import java.util.List;

public interface CrudService<T> {
    void crear(T entidad);
    T buscarPorId(Long id);
    List<T> listarTodos();
    void actualizar(T entidad);
    void eliminar(Long id);
}