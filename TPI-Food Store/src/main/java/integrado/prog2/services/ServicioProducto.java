package integrado.prog2.services;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exceptions.ValidacionException;
import integrado.prog2.services.Crud_Generico.CrudGenerico;
import java.util.List;

public class ServicioProducto extends CrudGenerico<Producto> {
    
    private final ServicioCategoria servicioCategoria;
    public ServicioProducto(ServicioCategoria servicioCategoria) {
        this.servicioCategoria = servicioCategoria;
    }

    @Override
    protected void validarCreacion(Producto producto) {
        validarCategoriaExistente(producto.getCategoria());
    }

    @Override
    protected void aplicarCambios(Producto existente, Producto nuevaInfo) {
        existente.setNombre(nuevaInfo.getNombre());
        existente.setPrecio(nuevaInfo.getPrecio());
        existente.setDescripcion(nuevaInfo.getDescripcion());
        existente.setStock(nuevaInfo.getStock());
        existente.setImagen(nuevaInfo.getImagen());
        existente.setDisponible(nuevaInfo.isDisponible());
        existente.setCategoria(nuevaInfo.getCategoria());
    }

    @Override
    protected void validarActualizacion(Producto existente, Producto nuevaInfo) {
        validarCategoriaExistente(nuevaInfo.getCategoria());
    }

    private void validarCategoriaExistente(Categoria categoria) {
        if (categoria == null) throw new ValidacionException("El producto debe pertenecer a una categoria."); 
        if (!servicioCategoria.existeCategoriaActiva(categoria.getId()))  throw new ValidacionException("La categoria especificada no existe o fue eliminada.");    
    }
    
    public boolean existeProductoActivo(Long id) {
        return datos.stream().anyMatch(prod -> prod.getId().equals(id) && !prod.isEliminado());
    }
    
    public List<Producto> listarPorCategoria(Long idCategoria) {
        if (!servicioCategoria.existeCategoriaActiva(idCategoria)) throw new ValidacionException("La categoria no existe o fue eliminada.");   
        return datos.stream().filter(p -> !p.isEliminado() && p.getCategoria() != null && p.getCategoria().getId().equals(idCategoria)).toList();
    }
}