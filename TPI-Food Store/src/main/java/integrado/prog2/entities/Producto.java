package integrado.prog2.entities;
import integrado.prog2.utils.Utilitario;
import java.util.Objects;

public final class Producto extends Base {
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;
    public Producto(String nombre, Double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria) {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);
        this.disponible = disponible;
        setCategoria(categoria);
        
        //this.categoria = categoria;
        //if (categoria != null) categoria.agregarProducto(this);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        Utilitario.validarTextoNoVacio(nombre, "nombre");
        this.nombre = nombre; 
    }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { 
        Utilitario.validarNoNegativo(precio, "precio");
        this.precio = precio; 
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        Utilitario.validarTextoNoVacio(descripcion, "descripcion");
        this.descripcion = descripcion; 
    }

    public int getStock() { return stock; }
    public void setStock(int stock) { 
        Utilitario.validarNoNegativo(stock, "stock");
        this.stock = stock; 
    }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { 
        Utilitario.validarTextoNoVacio(imagen, "imagen");
        this.imagen = imagen; 
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        if (Objects.equals(this.categoria, categoria)) return;      
        if (this.categoria != null) this.categoria.getProductos().remove(this);
        
        this.categoria = categoria;
        if (categoria != null && !categoria.getProductos().contains(this)) categoria.getProductos().add(this);     
    }

    @Override
    public String toString() {
        return String.format("Producto #%d: %s | Precio: $%.2f | Stock: %d | Categoria: %s | %s", getId(), nombre, precio, stock,
                categoria != null ? categoria.getNombre() : "Sin categoria", disponible ? "Disponible" : "No disponible");
    }
}