package integrado.prog2;

import integrado.prog2.services.ServicioCategoria;
import integrado.prog2.services.ServicioProducto;
import integrado.prog2.services.ServicioUsuario;
import integrado.prog2.services.ServicioPedido;
import integrado.prog2.ui.Menu;
import integrado.prog2.utils.DataSeed;

public class Main {
    public static void main(String[] args) {
        ServicioCategoria servicioCategoria = new ServicioCategoria();
        ServicioProducto servicioProducto = new ServicioProducto(servicioCategoria);
        ServicioUsuario servicioUsuario = new ServicioUsuario();
        ServicioPedido servicioPedido = new ServicioPedido(servicioUsuario, servicioProducto);

        Menu menu = new Menu(servicioCategoria, servicioProducto, servicioUsuario, servicioPedido);
        DataSeed.inicializar(servicioCategoria, servicioProducto, servicioUsuario, servicioPedido);
        menu.iniciar();
    }
}