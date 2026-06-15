package integrado.prog2.ui;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exceptions.*;
import integrado.prog2.services.ServicioCategoria;
import integrado.prog2.utils.Utilitario;
import java.util.List;
import java.util.Scanner;

public final class MenuCategoria {
    private MenuCategoria(){}
    public static void mostrar(Scanner scanner, ServicioCategoria servicioCategoria) {
        int opcion = -1;
        do {
            System.out.println("\n--- CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listarCategorias(servicioCategoria);
                    case 2 -> crearCategoria(scanner, servicioCategoria);
                    case 3 -> editarCategoria(scanner, servicioCategoria);
                    case 4 -> eliminarCategoria(scanner, servicioCategoria);
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida. Porfavor elija una opcion del 0 al 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
            }
        } while (opcion != 0);
    }

    private static void listarCategorias(ServicioCategoria servicioCategoria) {
        List<Categoria> lista = servicioCategoria.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay categorias cargadas");
            return;
        }
        for (Categoria c : lista) {
            System.out.println(c);
        }
    }

    private static void crearCategoria(Scanner scanner, ServicioCategoria servicioCategoria) {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            Utilitario.validarTextoNoVacio(nombre, "nombre");
            
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
            Utilitario.validarTextoNoVacio(descripcion, "descripcion");
            
            Categoria categoria = new Categoria(nombre, descripcion);
            servicioCategoria.crear(categoria);
            System.out.println("Categoria creada con exito. Id: " + categoria.getId());
        } catch (TextoInvalidoException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void editarCategoria(Scanner scanner, ServicioCategoria servicioCategoria) {
        try {
            List<Categoria> lista = servicioCategoria.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay categorias cargadas");
                return;
            }
            for (Categoria c : lista) {
                System.out.println(c);
            }
            System.out.print("ID de la categoria a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Categoria existente = servicioCategoria.buscarPorId(id);
            System.out.print("Nuevo nombre (deje vacio para mantener el nombre '" + existente.getNombre() + "'): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripcion (deje vacio para mantener la descripcion '" + existente.getDescripcion() + "'): ");
            String descripcion = scanner.nextLine();
            if (nombre.isBlank() && descripcion.isBlank()) {
                System.out.println("No se hizo ninguna actualizacion");
                return;
            }
            if (!nombre.isBlank()) existente.setNombre(nombre);
            if (!descripcion.isBlank()) existente.setDescripcion(descripcion);
            servicioCategoria.actualizar(existente);
            System.out.println("Categoria actualizada con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException | TextoInvalidoException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void eliminarCategoria(Scanner scanner, ServicioCategoria servicioCategoria) {
        try {
            List<Categoria> lista = servicioCategoria.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay categorias cargadas");
                return;
            }
            System.out.print("Desea ver la lista de categorias? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                for (Categoria c : lista) {
                    System.out.println(c);
                }
            }
            System.out.print("Introduzca el Id de la categoria a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            servicioCategoria.buscarPorId(id);
            
            System.out.print("Seguro que quiere eliminar esta categoria? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Eliminacion cancelada");
                return;
            }
            servicioCategoria.eliminar(id);
            System.out.println("La categoria se elimino con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }
}