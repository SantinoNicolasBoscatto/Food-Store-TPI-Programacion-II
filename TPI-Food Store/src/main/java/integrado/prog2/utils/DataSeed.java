package integrado.prog2.utils;

import integrado.prog2.entities.*;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.services.*;
import integrado.prog2.services.ServicioPedido.DetalleInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSeed {

    public static void inicializar(ServicioCategoria servicioCategoria, ServicioProducto servicioProducto, ServicioUsuario servicioUsuario, ServicioPedido servicioPedido) 
    {
        Categoria bebidas = new Categoria("Bebidas", "Gaseosas, jugos, agua mineral");
        Categoria pizzas = new Categoria("Pizzas", "Pizzas al horno de barro");
        Categoria postres = new Categoria("Postres", "Tortas, helados, flanes");
        servicioCategoria.crear(bebidas);
        servicioCategoria.crear(pizzas);
        servicioCategoria.crear(postres);

        Producto cocaCola = new Producto("Coca Cola 500ml", 2500.0, "Botella de 500ml", 100, "coca.jpg", true, bebidas);
        Producto sprite = new Producto("Sprite 500ml", 2300.0, "Botella de 500ml", 80, "sprite.jpg", true, bebidas);
        Producto aguaMineral = new Producto("Agua Mineral 1L", 1500.0, "Botella de 1 litro", 200, "agua.jpg", true, bebidas);
        Producto pizzaMuzzarella = new Producto("Pizza Muzzarella", 8500.0, "Grande de 8 porciones", 10, "muzza.jpg", true, pizzas);
        Producto pizzaNapolitana = new Producto("Pizza Napolitana", 9500.0, "Grande de 8 porciones", 5, "napo.jpg", true, pizzas);
        Producto flanCasero = new Producto("Flan Casero", 3200.0, "Con caramelo y crema", 15, "flan.jpg", true, postres);
        Producto heladoChocolate = new Producto("Helado Chocolate 1/4kg", 4000.0, "Artesanal", 0, "helado.jpg", false, postres);

        servicioProducto.crear(cocaCola);
        servicioProducto.crear(sprite);
        servicioProducto.crear(aguaMineral);
        servicioProducto.crear(pizzaMuzzarella);
        servicioProducto.crear(pizzaNapolitana);
        servicioProducto.crear(flanCasero);
        servicioProducto.crear(heladoChocolate);

        Usuario santino = new Usuario("Santino", "Boscatto", "santinoboscatto05@gmail.com", "3517310272", "admin123", Rol.ADMIN);
        Usuario enzo = new Usuario("Enzo", "Vitale", "enzoCARP912@gmail.com", "3516249841", "enzoVitale", Rol.USUARIO);
        Usuario jere = new Usuario("Jeremias", "Brochero", "jerreco30@gmail.com", "3515683072", "jereBrochero", Rol.USUARIO);
        servicioUsuario.crear(santino);
        servicioUsuario.crear(enzo);
        servicioUsuario.crear(jere);


        List<DetalleInfo> detallesPedido1 = new ArrayList<>();
        detallesPedido1.add(new DetalleInfo(2, cocaCola));
        detallesPedido1.add(new DetalleInfo(1, pizzaMuzzarella));
        servicioPedido.crearPedidoCompleto(LocalDate.of(2026, 6, 10), Estado.PENDIENTE, FormaPago.TARJETA, enzo, detallesPedido1);

        List<DetalleInfo> detallesPedido2 = new ArrayList<>();
        detallesPedido2.add(new DetalleInfo(3, sprite));
        detallesPedido2.add(new DetalleInfo(1, flanCasero));
        servicioPedido.crearPedidoCompleto(LocalDate.of(2026, 6, 11), Estado.CONFIRMADO, FormaPago.EFECTIVO, jere, detallesPedido2);

        List<DetalleInfo> detallesPedido3 = new ArrayList<>();
        detallesPedido3.add(new DetalleInfo(1, pizzaNapolitana));
        detallesPedido3.add(new DetalleInfo(2, aguaMineral));
        servicioPedido.crearPedidoCompleto(LocalDate.of(2026, 6, 9), Estado.TERMINADO, FormaPago.TRANSFERENCIA, enzo, detallesPedido3);

        List<DetalleInfo> detallesPedido4 = new ArrayList<>();
        detallesPedido4.add(new DetalleInfo(1, heladoChocolate));
        servicioPedido.crearPedidoCompleto(LocalDate.of(2026, 6, 8), Estado.CANCELADO, FormaPago.TARJETA, santino, detallesPedido4);
    }
}