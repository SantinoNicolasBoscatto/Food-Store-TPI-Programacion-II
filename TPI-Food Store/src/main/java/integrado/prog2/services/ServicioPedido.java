package integrado.prog2.services;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import integrado.prog2.exceptions.ValidacionException;
import integrado.prog2.services.Crud_Generico.CrudGenerico;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/* La historia dice que al crear un pedido con detalles, si ocurre una excepción al agregar un detalle se debe capturar el error y cancelar la creación del pedido
en memoria para no dejar datos inconsistentes.

crearPedidoCompleto recibe todos los datos necesarios, hace las validaciones correspondientes, construye el objeto Pedido y le va agregando los detalles 
mediante addDetallePedido y si todo es exitoso, recién ahí agrega el pedido con todos sus detalles. Pero si falla lanza la excepción correspondiente y no se 
inserta nada en la lista (atomicidad).
*/
public class ServicioPedido extends CrudGenerico<Pedido> {

    private final ServicioUsuario servicioUsuario;
    private final ServicioProducto servicioProducto;

    public ServicioPedido(ServicioUsuario servicioUsuario, ServicioProducto servicioProducto) {
        this.servicioUsuario = servicioUsuario;
        this.servicioProducto = servicioProducto;
    }

    @Override
    protected void validarCreacion(Pedido pedido) {
        validarUsuarioExistente(pedido.getUsuario());
        if (pedido.getDetalles().isEmpty()) throw new ValidacionException("El pedido debe tener al menos un detalle.");
    }

    @Override
    protected void aplicarCambios(Pedido existente, Pedido nuevaInfo) {
        existente.setEstado(nuevaInfo.getEstado());
        existente.setFormaPago(nuevaInfo.getFormaPago());
    }

    @Override
    protected void validarActualizacion(Pedido existente, Pedido nuevaInfo) {
        if (!existente.getUsuario().equals(nuevaInfo.getUsuario())) throw new ValidacionException("No se puede cambiar el usuario de un pedido"); 
        if (!existente.getFecha().equals(nuevaInfo.getFecha())) throw new ValidacionException("No se puede cambiar la fecha de un pedido");     
        validarTransicionEstado(existente.getEstado(), nuevaInfo.getEstado());
    }
    
    public Pedido crearPedidoCompleto(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario, List<DetalleInfo> detalles) 
    {
      if (detalles == null || detalles.isEmpty()) throw new ValidacionException("El pedido debe tener al menos un detalle");
      validarUsuarioExistente(usuario);

      for (DetalleInfo d : detalles) {
          if (!servicioProducto.existeProductoActivo(d.producto.getId())) {
              throw new EntidadNoEncontradaException("El producto con ID " + d.producto.getId() + " no existe o no está disponible");
          }
      }

      Pedido pedido = new Pedido(fecha, estado, formaPago, usuario);
      List<DetallePedido> detallesAgregados = new ArrayList<>();
      
      try {
          for (DetalleInfo d : detalles) {
              pedido.addDetallePedido(d.cantidad, d.producto);          
              detallesAgregados.add(pedido.getDetalles().get(pedido.getDetalles().size() - 1));
          }
      } 
      catch (RuntimeException e) {     
          for (DetallePedido detalle : detallesAgregados) {
              Producto prod = detalle.getProducto();
              prod.setStock(prod.getStock() + detalle.getCantidad());
          }       
          usuario.getPedidos().remove(pedido);
          throw new ValidacionException("Error al crear el pedido: " + e.getMessage());
      }

      super.crear(pedido);
      return pedido;
  }

    public void agregarDetalle(Long idPedido, int cantidad, Producto producto) {
        Pedido pedido = buscarPorId(idPedido);
        if (!servicioProducto.existeProductoActivo(producto.getId())) throw new EntidadNoEncontradaException("El producto no existe o no esta disponible");
        pedido.addDetallePedido(cantidad, producto);
    }

    public void eliminarDetalle(Long idPedido, Producto producto) {
        Pedido pedido = buscarPorId(idPedido);
        DetallePedido detalle = pedido.findDetallePedidoByProducto(producto);
        if (detalle == null) throw new EntidadNoEncontradaException("El producto no esta en el pedido");  
        pedido.deleteDetallePedidoByProducto(producto);
    }
    
    @Override
    public void eliminar(Long id) {
        Pedido pedido = buscarPorId(id);
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setEliminado(true);
        }
        super.eliminar(id);
    }

    public List<DetallePedido> listarDetalles(Long idPedido) {
        Pedido pedido = buscarPorId(idPedido);
        return pedido.getDetalles();
    }

    private void validarUsuarioExistente(Usuario usuario) {
        if (usuario == null) throw new ValidacionException("El pedido debe pertenecer a un usuario");      
        if (!servicioUsuario.existeUsuarioActivo(usuario.getId())) throw new EntidadNoEncontradaException("El usuario especificado no existe o fue eliminado");       
    }
    
    public List<Pedido> listarPorUsuario(Long idUsuario) {
        if (!servicioUsuario.existeUsuarioActivo(idUsuario)) throw new EntidadNoEncontradaException("El usuario no existe o fue eliminado.");     
        return datos.stream().filter(p -> !p.isEliminado() && p.getUsuario() != null && p.getUsuario().getId().equals(idUsuario)).toList();
    }
    
    public static class DetalleInfo {
        public final int cantidad;
        public final Producto producto;

        public DetalleInfo(int cantidad, Producto producto) {
            this.cantidad = cantidad;
            this.producto = producto;
        }
    }
    
    public void validarTransicionEstado(Estado actual, Estado nuevo) {
        if (actual == nuevo) return; 
        switch (actual) {
            case PENDIENTE:
                if (nuevo != Estado.CONFIRMADO && nuevo != Estado.CANCELADO) throw new ValidacionException("Un pedido PENDIENTE solo puede pasar a CONFIRMADO o CANCELADO");        
                break;
            case CONFIRMADO:
                if (nuevo != Estado.TERMINADO && nuevo != Estado.CANCELADO) throw new ValidacionException("Un pedido CONFIRMADO solo puede pasar a TERMINADO o CANCELADO");               
                break;
            case TERMINADO:
            case CANCELADO:
                throw new ValidacionException("Un pedido " + actual + " no puede cambiar de estado");
            default:
                throw new ValidacionException("Estado desconocido");
        }
    }
    
    public void actualizarEstadoYFormaPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago) {
        Pedido pedido = buscarPorId(id);
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);
        pedido.setEstado(nuevoEstado);
        pedido.setFormaPago(nuevaFormaPago);
    }
}