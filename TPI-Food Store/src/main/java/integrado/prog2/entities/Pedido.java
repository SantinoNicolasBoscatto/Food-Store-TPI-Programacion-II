package integrado.prog2.entities;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.interfaces.Calculable;
import integrado.prog2.utils.Utilitario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles;
    private Usuario usuario;
    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        setFecha(fecha);
        setEstado(estado);
        setFormaPago(formaPago);
        setUsuario(usuario);
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) {
        if (fecha == null) throw new IllegalArgumentException("La fecha no puede ser nula");     
        this.fecha = fecha;
    }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) {
        if (estado == null) throw new IllegalArgumentException("El estado no puede ser nulo");
        this.estado = estado;
    }

    public Double getTotal() { return total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) {
        if (formaPago == null) throw new IllegalArgumentException("La forma de pago no puede ser nula");     
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() { return detalles; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");  
        if (this.usuario != null) this.usuario.getPedidos().remove(this);
        
        this.usuario = nuevoUsuario;
        if (!nuevoUsuario.getPedidos().contains(this)) nuevoUsuario.getPedidos().add(this);     
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nul");      
        Utilitario.validarPositivo(cantidad, "cantidad");

        DetallePedido detalle = new DetallePedido(cantidad, producto);
        detalle.asignarId();  
        this.detalles.add(detalle);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo");
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) return detalle;         
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalleAEliminar = findDetallePedidoByProducto(producto);
        if (detalleAEliminar != null) {
            this.detalles.remove(detalleAEliminar);
            calcularTotal();
        }
    }

    @Override
    public void calcularTotal() {
        this.total = 0.0;
        for (DetallePedido detalle : detalles) {
            this.total += detalle.getSubtotal();
        }
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d | Usuario: %s %s (%d) | Fecha: %s | Estado: %s | FormaPago: %s | Total: $%.2f",
                getId(), usuario != null ? usuario.getNombre() : "?", usuario != null ? usuario.getApellido() : "", 
                usuario != null ? usuario.getId() : -1, fecha, estado, formaPago, total);
    }
}