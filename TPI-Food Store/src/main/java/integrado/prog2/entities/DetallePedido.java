package integrado.prog2.entities;
import integrado.prog2.utils.Utilitario;

public final class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;
    public DetallePedido(int cantidad, Producto producto) {
        super();
        setCantidad(cantidad);
        setProducto(producto);
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        Utilitario.validarPositivo(cantidad, "cantidad");
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public Double getSubtotal() { return subtotal; }     
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo");
        this.producto = producto;
        recalcularSubtotal();
    }
    
    private void recalcularSubtotal() {
        if (producto != null) this.subtotal = this.cantidad * producto.getPrecio();      
        else this.subtotal = 0.0;    
    }

    @Override
    public String toString() {
        if (producto == null) return "Producto es null";      
        return String.format("DetallePedido #%d: %s x %d => Subtotal: $%.2f", getId(), producto.getNombre(), cantidad, subtotal);
    }
}