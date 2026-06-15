package integrado.prog2.ui;
import integrado.prog2.services.*;
import java.util.Scanner;

public class Menu {
    private final ServicioCategoria servicioCategoria;
    private final ServicioProducto servicioProducto;
    private final ServicioUsuario servicioUsuario;
    private final ServicioPedido servicioPedido;
    private final Scanner scanner;
    public Menu(ServicioCategoria servicioCategoria, ServicioProducto servicioProducto, ServicioUsuario servicioUsuario, ServicioPedido servicioPedido) 
    {
        this.servicioCategoria = servicioCategoria;
        this.servicioProducto = servicioProducto;
        this.servicioUsuario = servicioUsuario;
        this.servicioPedido = servicioPedido;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion = -1;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> MenuCategoria.mostrar(scanner, servicioCategoria);
                    case 2 -> MenuProducto.mostrar(scanner, servicioProducto, servicioCategoria);
                    case 3 -> MenuUsuario.mostrar(scanner, servicioUsuario);
                    case 4 -> MenuPedido.mostrar(scanner, servicioPedido, servicioUsuario, servicioProducto);
                    case 0 -> System.out.println("Saliendo del sistema!");
                    default -> System.out.println("La opcion seleccionada es invalida, porfavor intente nuevamente. Elija una opcion del 0 al 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, debe ingresar un numero. Intente de nuevo");
            }
        } while (opcion != 0);
    }    
}