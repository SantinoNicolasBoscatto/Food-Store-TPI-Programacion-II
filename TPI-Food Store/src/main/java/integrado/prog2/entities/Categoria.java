package integrado.prog2.entities;
import integrado.prog2.utils.Utilitario;
import java.util.ArrayList;
import java.util.List;

public final class Categoria extends Base {
    private String nombre;
    private String descripcion;
    private List<Producto> productos;
    public Categoria(String nombre, String descripcion) {
        super();
        setNombre(nombre);
        setDescripcion(descripcion);
        this.productos = new ArrayList<>();
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {
        Utilitario.validarTextoNoVacio(nombre, "nombre categoria");
        this.nombre = nombre;
    }

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {
        Utilitario.validarTextoNoVacio(descripcion, "descripcion");
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {return productos;}
    public void setProductos(List<Producto> productos) {
        for (Producto p : new ArrayList<>(this.productos)) {
            p.setCategoria(null);
        }
        this.productos = productos;
        if (productos != null) {
            for (Producto p : productos) {
                p.setCategoria(this);
            }
        }
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo");
        if (!this.productos.contains(producto)) {
            this.productos.add(producto);
            producto.setCategoria(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Categoria #%d: %s - %s [%d productos]", getId(), nombre, descripcion, productos.size());
    }
}