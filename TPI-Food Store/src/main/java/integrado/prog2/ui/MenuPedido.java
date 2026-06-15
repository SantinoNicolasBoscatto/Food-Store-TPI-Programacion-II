package integrado.prog2.ui;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exceptions.*;
import integrado.prog2.services.ServicioPedido;
import integrado.prog2.services.ServicioPedido.DetalleInfo;
import integrado.prog2.services.ServicioProducto;
import integrado.prog2.services.ServicioUsuario;
import integrado.prog2.utils.Utilitario;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class MenuPedido {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private MenuPedido() {}

    public static void mostrar(Scanner scanner, ServicioPedido servicioPedido, ServicioUsuario servicioUsuario, ServicioProducto servicioProducto) 
    {
        int opcion = -1;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");            
            System.out.println("5. Ver detalles");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listarPedidos(scanner, servicioPedido);
                    case 2 -> crearPedido(scanner, servicioPedido, servicioUsuario, servicioProducto);
                    case 3 -> editarPedido(scanner, servicioPedido);
                    case 4 -> eliminarPedido(scanner, servicioPedido);                    
                    case 5 -> verDetallesPedido(scanner, servicioPedido);
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida. Porfavor elija una opcion del 0 al 5");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
            }
        } while (opcion != 0);
    }

    private static void listarPedidos(Scanner scanner, ServicioPedido servicioPedido) 
    {
        System.out.print("Filtrar por usuario? (S/N): ");
        String filtro = scanner.nextLine();
        List<Pedido> lista;
        if (filtro.equalsIgnoreCase("S")) {
            try {
                System.out.print("Id del usuario: ");
                Long idUsuario = Long.parseLong(scanner.nextLine());
                lista = servicioPedido.listarPorUsuario(idUsuario);
            } catch (NumberFormatException e) {
                System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
                return;
            } catch (EntidadNoEncontradaException e) {
                System.out.println("Error, " + e.getMessage());
                return;
            }
        } 
        else lista = servicioPedido.listarTodos();
        

        if (lista.isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }
        for (Pedido p : lista) {
            System.out.println(p);
        }
    }

    private static void crearPedido(Scanner scanner, ServicioPedido servicioPedido, ServicioUsuario servicioUsuario, ServicioProducto servicioProducto) {
        try {
            System.out.print("Id del usuario: ");
            Long idUsuario = Long.parseLong(scanner.nextLine());
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);

            System.out.print("Fecha (El formato valido es: dd/MM/yyyy): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine(), FORMATO_FECHA);

            System.out.println("Estado del pedido:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");
            System.out.print("Seleccione un estado valido: ");
            int estadoOpcion = Integer.parseInt(scanner.nextLine());
            Estado estado = switch (estadoOpcion) {
                case 1 -> Estado.PENDIENTE;
                case 2 -> Estado.CONFIRMADO;
                case 3 -> Estado.TERMINADO;
                case 4 -> Estado.CANCELADO;
                default -> throw new ValidacionException("Selecciono un estado invalido.");
            };

            System.out.println("Formas de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            System.out.print("Seleccione una forma de pago valida: ");
            int fpOpcion = Integer.parseInt(scanner.nextLine());
            FormaPago formaPago = switch (fpOpcion) {
                case 1 -> FormaPago.TARJETA;
                case 2 -> FormaPago.TRANSFERENCIA;
                case 3 -> FormaPago.EFECTIVO;
                default -> throw new ValidacionException("Selecciono una forma de pago invalida.");
            };

            List<DetalleInfo> detalles = new ArrayList<>();
            boolean agregarOtro = true;
            while (agregarOtro) {
                System.out.print("Id del producto: ");
                Long idProducto = Long.parseLong(scanner.nextLine());
                Producto producto = servicioProducto.buscarPorId(idProducto);

                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                Utilitario.validarPositivo((double)cantidad, "cantidad");
                detalles.add(new DetalleInfo(cantidad, producto));

                System.out.print("Agregar otro producto? (S/N): ");
                String respuesta = scanner.nextLine();
                if (!respuesta.equalsIgnoreCase("S")) {
                    agregarOtro = false;
                }
            }

            Pedido pedido = servicioPedido.crearPedidoCompleto(fecha, estado, formaPago, usuario, detalles);
            System.out.println("Pedido creado con exito. Id: " + pedido.getId());

        } catch (NumberFormatException e) {
            System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
        } catch (DateTimeParseException e) {
            System.out.println("Error, formato de fecha invalido. Use dd/MM/yyyy");
        } catch (EntidadNoEncontradaException | ValidacionException |
                 NumeroNoPositivoException | NumeroNegativoException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void editarPedido(Scanner scanner, ServicioPedido servicioPedido) {
        try {
            List<Pedido> lista = servicioPedido.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay pedidos cargados");
                return;
            }
            System.out.println("Listado de pedidos: ");
            for (Pedido p : lista) {
                System.out.println(p);
            }
            System.out.print("Introduzca el Id del pedido a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            servicioPedido.buscarPorId(id);       
            
            Pedido existente = servicioPedido.buscarPorId(id);
            System.out.println(existente);

            System.out.println("Nuevo estado:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");
            System.out.print("Seleccione (deje vacio para mantener el estado '" + existente.getEstado() + "'): ");
            String estadoString = scanner.nextLine();
            Estado nuevoEstado = existente.getEstado();
            if (!estadoString.isBlank()) {
                int estOpcion = Integer.parseInt(estadoString);
                nuevoEstado = switch (estOpcion) {
                    case 1 -> Estado.PENDIENTE;
                    case 2 -> Estado.CONFIRMADO;
                    case 3 -> Estado.TERMINADO;
                    case 4 -> Estado.CANCELADO;
                    default -> throw new ValidacionException("Estado invalido. Las opciones validas son del 1 al 4");
                };
                servicioPedido.validarTransicionEstado(existente.getEstado(), nuevoEstado);
            }

            System.out.println("Nueva forma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            System.out.print("Seleccione (deje vacio para mantener el metodo de pago '" + existente.getFormaPago() + "'): ");
            String formaPagoString = scanner.nextLine();
            FormaPago nuevaFormaPago = existente.getFormaPago();
            if (!formaPagoString.isBlank()) {
                int fpOpcion = Integer.parseInt(formaPagoString);
                nuevaFormaPago = switch (fpOpcion) {
                    case 1 -> FormaPago.TARJETA;
                    case 2 -> FormaPago.TRANSFERENCIA;
                    case 3 -> FormaPago.EFECTIVO;
                    default -> throw new ValidacionException("Forma de pago invalida. Las opciones validas son del 1 al 3");
                };
            }

            if (nuevoEstado == existente.getEstado() && nuevaFormaPago == existente.getFormaPago()) {
                System.out.println("No se hizo ninguna actualizacion.");
                return;
            }

            servicioPedido.actualizarEstadoYFormaPago(id, nuevoEstado, nuevaFormaPago);
            System.out.println("Pedido actualizado con exito.");

        } catch (NumberFormatException e) {
            System.out.println("Error, el formato es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void eliminarPedido(Scanner scanner, ServicioPedido servicioPedido) {
        try {
            List<Pedido> lista = servicioPedido.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay pedidos cargados");
                return;
            }
            System.out.print("Desea ver la lista de pedidos? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                for (Pedido p : lista) {
                    System.out.println(p);
                }
            }
            System.out.print("Introduzca el Id del pedido a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            servicioPedido.buscarPorId(id);
            
            System.out.print("Seguro que quiere eliminar este pedido? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Eliminacion cancelada");
                return;
            }
            servicioPedido.eliminar(id);
            System.out.println("El pedido se elimino con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }
    
    private static void verDetallesPedido(Scanner scanner, ServicioPedido servicioPedido) 
    {
        try 
        {
            List<Pedido> lista = servicioPedido.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay pedidos cargados");
                return;
            }
            for (Pedido p : lista) {
                System.out.println(p);
            }
            System.out.print("Id del pedido para ver sus detalles: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            List<DetallePedido> detalles = servicioPedido.listarDetalles(id);
            if (detalles.isEmpty()) System.out.println("El pedido no tiene detalles.");
            else {
                for (DetallePedido d : detalles) {
                    System.out.println(d);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }
}