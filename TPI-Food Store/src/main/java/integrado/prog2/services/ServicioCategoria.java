package integrado.prog2.services;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exceptions.ValidacionException;
import integrado.prog2.services.Crud_Generico.CrudGenerico;

public class ServicioCategoria extends CrudGenerico<Categoria> {

    @Override
    protected void validarCreacion(Categoria categoria) {
        for (Categoria cat : datos) {
            if (!cat.isEliminado() && cat.getNombre().equalsIgnoreCase(categoria.getNombre())) 
                throw new ValidacionException("Ya existe una categoria con el nombre " + categoria.getNombre());         
        }
    }
    
    @Override
    protected void validarActualizacion(Categoria existente, Categoria nuevaInfo) {
        for (Categoria cat : datos) {
            if (!cat.isEliminado() && cat.getNombre().equalsIgnoreCase(nuevaInfo.getNombre()) && !cat.getId().equals(existente.getId())) 
                throw new ValidacionException("Ya existe otra categoria con el nombre " + nuevaInfo.getNombre());         
        }
    }

    @Override
    protected void aplicarCambios(Categoria existente, Categoria nuevaInfo) {
        existente.setNombre(nuevaInfo.getNombre());
        existente.setDescripcion(nuevaInfo.getDescripcion());
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        boolean tieneProductosActivos = categoria.getProductos().stream().anyMatch(p -> !p.isEliminado());
        if (tieneProductosActivos) 
            throw new ValidacionException("No se puede eliminar la categoria porque tiene productos activos");
        
        super.eliminar(id);
    }
    
    public boolean existeCategoriaActiva(Long id) {
        return datos.stream().anyMatch(cat -> cat.getId().equals(id) && !cat.isEliminado());
    }
}