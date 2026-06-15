package integrado.prog2.entities;
import integrado.prog2.enums.Rol;
import integrado.prog2.utils.Utilitario;
import java.util.ArrayList;
import java.util.List;

public final class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;
    private List<Pedido> pedidos;
    public Usuario(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) 
    {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setMail(mail);
        setCelular(celular);
        setContrasenia(contrasenia);
        setRol(rol);
        this.pedidos = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        Utilitario.validarTextoNoVacio(nombre, "nombre");
        this.nombre = nombre; 
    }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { 
        Utilitario.validarTextoNoVacio(apellido, "apellido");
        this.apellido = apellido; 
    }

    public String getMail() { return mail; }
    public void setMail(String mail) { 
        Utilitario.validarTextoNoVacio(mail, "mail");
        Utilitario.validarFormatoMail(mail, "mail");
        this.mail = mail; 
    }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { 
        Utilitario.validarTextoNoVacio(celular, "celular");
        this.celular = celular; 
    }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) {
        Utilitario.validarTextoNoVacio(contrasenia, "contrasenia");
        this.contrasenia = contrasenia; 
    }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { 
        if (rol == null) throw new IllegalArgumentException("El rol no puede ser nulo");
        this.rol = rol; 
    }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) {
        for (Pedido p : new ArrayList<>(this.pedidos)) {
            p.setUsuario(null);
        }
        this.pedidos = pedidos;
        if (pedidos != null) for (Pedido p : pedidos) { p.setUsuario(this); }     
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido != null && !this.pedidos.contains(pedido)) {
            this.pedidos.add(pedido);
            pedido.setUsuario(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Usuario #%d: %s %s | Mail: %s | Rol: %s", getId(), nombre, apellido, mail, rol);
    }
}