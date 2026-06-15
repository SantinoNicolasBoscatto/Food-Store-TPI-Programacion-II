package integrado.prog2.ui;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exceptions.*;
import integrado.prog2.services.ServicioUsuario;
import integrado.prog2.utils.Utilitario;
import java.util.List;
import java.util.Scanner;

public final class MenuUsuario {
    private MenuUsuario() {}
    public static void mostrar(Scanner scanner, ServicioUsuario servicioUsuario) {
        int opcion = -1;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listarUsuarios(servicioUsuario);
                    case 2 -> crearUsuario(scanner, servicioUsuario);
                    case 3 -> editarUsuario(scanner, servicioUsuario);
                    case 4 -> eliminarUsuario(scanner, servicioUsuario);
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida. Porfavor elija una opcion del 0 al 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
            }
        } while (opcion != 0);
    }

    private static void listarUsuarios(ServicioUsuario servicioUsuario) {
        List<Usuario> lista = servicioUsuario.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }
        for (Usuario u : lista) {
            System.out.println(u);
        }
    }

    private static void crearUsuario(Scanner scanner, ServicioUsuario servicioUsuario) {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            Utilitario.validarTextoNoVacio(nombre, "nombre");
            
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            Utilitario.validarTextoNoVacio(apellido, "apellido");
            
            System.out.print("Mail: ");
            String mail = scanner.nextLine();
            Utilitario.validarTextoNoVacio(mail, "mail");
            
            System.out.print("Celular: ");
            String celular = scanner.nextLine();
            Utilitario.validarTextoNoVacio(celular, "celular");
            
            System.out.print("Contrasenia: ");
            String contrasenia = scanner.nextLine();
            Utilitario.validarTextoNoVacio(contrasenia, "contrasenia");
            
            System.out.print("Rol (1. ADMIN, 2. USUARIO): ");
            int rolOpcion = Integer.parseInt(scanner.nextLine());
            Rol rol = switch (rolOpcion) {
                case 1 -> Rol.ADMIN;
                case 2 -> Rol.USUARIO;
                default -> throw new ValidacionException("Rol invalido. Porfavor elija una opcion valida: Rol (1. ADMIN, 2. USUARIO)");
            };

            Usuario usuario = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
            servicioUsuario.crear(usuario);
            System.out.println("Usuario creado con exito. Id: " + usuario.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
        } catch (TextoInvalidoException | MailDuplicadoException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void editarUsuario(Scanner scanner, ServicioUsuario servicioUsuario) {
        try {
            List<Usuario> lista = servicioUsuario.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay usuarios cargados");
                return;
            }
            for (Usuario u : lista) {
                System.out.println(u);
            }
            System.out.print("Id del usuario a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Usuario existente = servicioUsuario.buscarPorId(id);

            System.out.print("Nuevo nombre (deje vacio para mantener el nombre '" + existente.getNombre() + "'): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo apellido (deje vacio para mantener el apellido '" + existente.getApellido() + "'): ");
            String apellido = scanner.nextLine();
            System.out.print("Nuevo mail (deje vacio para mantener el mail '" + existente.getMail() + "'): ");
            String mail = scanner.nextLine();
            System.out.print("Nuevo celular (deje vacio para mantener el celular '" + existente.getCelular() + "'): ");
            String celular = scanner.nextLine();
            System.out.print("Nueva contrasenia (deje vacio para mantener la contrasenia '" + existente.getContrasenia() + "'): ");
            String contrasenia = scanner.nextLine();
            System.out.print("Nuevo rol (1. ADMIN, 2. USUARIO, deje vacio para mantener el rol '" + existente.getRol() + "'): ");
            String rolStr = scanner.nextLine();

            boolean algunCambio = false;

            if (!nombre.isBlank()) {
                existente.setNombre(nombre); 
                algunCambio = true; 
            }
            if (!apellido.isBlank()) {
                existente.setApellido(apellido); 
                algunCambio = true; 
            }
            if (!mail.isBlank()) {
                existente.setMail(mail); 
                algunCambio = true; 
            }
            if (!celular.isBlank()) {
                existente.setCelular(celular); 
                algunCambio = true; 
            }
            if (!contrasenia.isBlank()) {
                existente.setContrasenia(contrasenia); 
                algunCambio = true; 
            }
            if (!rolStr.isBlank()) {
                int rolOpcion = Integer.parseInt(rolStr);
                Rol rol = switch (rolOpcion) {
                    case 1 -> Rol.ADMIN;
                    case 2 -> Rol.USUARIO;
                    default -> throw new ValidacionException("Rol invalido. Porfavor elija una opcion valida: Rol (1. ADMIN, 2. USUARIO)");
                };
                existente.setRol(rol);
                algunCambio = true;
            }

            if (!algunCambio) {
                System.out.println("No se hizo ninguna actualizacion.");
                return;
            }

            servicioUsuario.actualizar(existente);
            System.out.println("Usuario actualizado con exito");
        } catch (NumberFormatException e) {
            System.out.println("Error, debe ingresar un numero. Porfavor intente de nuevo");
        } catch (EntidadNoEncontradaException | TextoInvalidoException |
                 MailDuplicadoException | ValidacionException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }

    private static void eliminarUsuario(Scanner scanner, ServicioUsuario servicioUsuario) {
        try {
            List<Usuario> lista = servicioUsuario.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay usuarios cargados");
                return;
            }
            System.out.print("Desea ver la lista de usuarios? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                for (Usuario u : lista) {
                    System.out.println(u);
                }
            }
            System.out.print("Introduzca el Id del usuario a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            servicioUsuario.buscarPorId(id);  
            
            System.out.print("Seguro que quiere eliminar este usuario? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Eliminacion cancelada");
                return;
            }
            servicioUsuario.eliminar(id);
            System.out.println("El usuario se elimino con exito.");
        } catch (NumberFormatException e) {
            System.out.println("Error, el formato del Id es invalido, solo se aceptan numeros");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error, " + e.getMessage());
        }
    }
}