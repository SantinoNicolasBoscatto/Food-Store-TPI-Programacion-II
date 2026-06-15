package integrado.prog2.ui;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exceptions.*;
import integrado.prog2.services.ServicioCategoria;
import integrado.prog2.services.ServicioProducto;
import integrado.prog2.utils.Utilitario;
import java.util.List;
import java.util.Scanner;

public final class MenuProducto {
    private MenuProducto(){}
    
    public static void mostrar(Scanner scanner, ServicioProducto servicioProducto, ServicioCategoria servicioCategoria) 
    {
        int opcion = -1;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listarProductos(scanner, servicioProducto);
                    case 2 -> crearProducto(scanner, servicioProducto, servicioCategoria);
                    case 3 -> editarProducto(scanner, servicioProducto, servicioCategoria);
                    case 4 -> eliminarProducto(scanner, servicioProducto);
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida. Porfavor elija una opcion del 0 al 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
            }
        } while (opcion != 0);
    }

    private static void listarProductos(Scanner scanner, ServicioProducto servicioProducto) {
        System.out.print("Filtrar por categoria? (S/N): ");
        String filtro = scanner.nextLine();
        List<Producto> lista;
        if (filtro.equalsIgnoreCase("S")) {
            try {
                System.out.print("ID de la categoria: ");
                Long idCat = Long.parseLong(scanner.nextLine());
                lista = servicioProducto.listarPorCategoria(idCat);
            } catch (NumberFormatException e) {
                System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
                return;
            } catch (EntidadNoEncontradaException | ValidacionException e) {
                System.out.println("Error, " + e.getMessage());
                return;
            }
        } 
        else lista = servicioProducto.listarTodos();
        
        if (lista.isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }
       
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private static void crearProducto(Scanner scanner, ServicioProducto servicioProducto, ServicioCategoria servicioCategoria) 
    {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            Utilitario.validarTextoNoVacio(nombre, "nombre");
            
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
            Utilitario.validarTextoNoVacio(descripcion, "descripcion");
            
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            Utilitario.validarNoNegativo(precio, "precio");
            
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            Utilitario.validarNoNegativo(stock, "stock");
            
            System.out.print("Imagen: ");
            String imagen = scanner.nextLine();
            Utilitario.validarTextoNoVacio(imagen, "imagen");
            
            System.out.print("Disponible (S/N): ");
            boolean disponible = scanner.nextLine().equalsIgnoreCase("S");
            System.out.print("Id de la categoria: ");
            Long idCategoria = Long.parseLong(scanner.nextLine());

            Categoria categoria = servicioCategoria.buscarPorId(idCategoria);
            Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
            servicioProducto.crear(producto);
            System.out.println("Producto creado con exito. Id: " + producto.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException | TextoInvalidoException |
                 NumeroNegativoException | NumeroNoPositivoException |
                 ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void editarProducto(Scanner scanner, ServicioProducto servicioProducto, ServicioCategoria servicioCategoria) 
    {
        try {
            List<Producto> lista = servicioProducto.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay productos cargados");
                return;
            }
            for (Producto p : lista) {
                System.out.println(p);
            }
            System.out.print("Id del producto a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Producto existente = servicioProducto.buscarPorId(id);

            System.out.print("Nuevo nombre (deje vacio para mantener el nombre '" + existente.getNombre() + "'): ");
            String nombre = scanner.nextLine();
            
            System.out.print("Nueva descripcion (deje vacio para mantener la descripcion '" + existente.getDescripcion()+ "'): ");
            String descripcion = scanner.nextLine();
            
            System.out.print("Nuevo precio (deje vacio para mantener el precio '" + existente.getPrecio() + "'): ");
            String precioStr = scanner.nextLine();
            if(!precioStr.isBlank()) Utilitario.validarNoNegativo(Double.parseDouble(precioStr), "precio");
            
            System.out.print("Nuevo stock (deje vacio para mantener el stock '" + existente.getStock() + "'): ");
            String stockStr = scanner.nextLine();
            if(!stockStr.isBlank()) Utilitario.validarNoNegativo(Integer.parseInt(stockStr), "stock");
            
            System.out.print("Nueva imagen (deje vacio para mantener la imagen '" + existente.getImagen() + "'): ");
            String imagen = scanner.nextLine();
            System.out.print("Nuevo disponible S/N (deje vacio para mantener la disponibilidad " + (existente.isDisponible() ? "S" : "N") + "): ");
            String dispStr = scanner.nextLine();
            System.out.print("Nuevo Id de categoria (deje vacio para mantener la categoria " +
                    (existente.getCategoria() != null ? existente.getCategoria().getNombre() + "(Id: "+ existente.getCategoria().getId() +")" : 
                            "sin categoria") + "): ");
            String idCatStr = scanner.nextLine();

            boolean algunCambio = false;

            if (!nombre.isBlank()) { existente.setNombre(nombre); algunCambio = true; }
            if (!descripcion.isBlank()) { existente.setDescripcion(descripcion); algunCambio = true; }
            if (!precioStr.isBlank()) {
                double precio = Double.parseDouble(precioStr);
                existente.setPrecio(precio);
                algunCambio = true;
            }
            if (!stockStr.isBlank()) {
                int stock = Integer.parseInt(stockStr);
                existente.setStock(stock);
                algunCambio = true;
            }
            if (!imagen.isBlank()) { existente.setImagen(imagen); algunCambio = true; }
            if (!dispStr.isBlank()) {
                boolean disponible = dispStr.equalsIgnoreCase("S");
                existente.setDisponible(disponible);
                algunCambio = true;
            }
            if (!idCatStr.isBlank()) {
                Long idCategoria = Long.parseLong(idCatStr);
                Categoria categoria = servicioCategoria.buscarPorId(idCategoria);
                existente.setCategoria(categoria);
                algunCambio = true;
            }

            if (!algunCambio) {
                System.out.println("No se hizo ninguna actualizacion");
                return;
            }

            servicioProducto.actualizar(existente);
            System.out.println("Producto actualizado con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException | TextoInvalidoException |
                 NumeroNegativoException | NumeroNoPositivoException |
                 ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void eliminarProducto(Scanner scanner, ServicioProducto servicioProducto) {
        try {
            List<Producto> lista = servicioProducto.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay productos cargados");
                return;
            }
            System.out.print("Desea ver la lista de productos? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                for (Producto p : lista) {
                    System.out.println(p);
                }
            }
            System.out.print("Id del producto a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Producto producto = servicioProducto.buscarPorId(id);
            System.out.println(producto);
            System.out.print("Seguro que quiere eliminar este producto? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Eliminacion cancelada");
                return;
            }
            servicioProducto.eliminar(id);
            System.out.println("El producto se elimino con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }
}