package integrado.prog2.services;
import integrado.prog2.entities.Usuario;
import integrado.prog2.exceptions.MailDuplicadoException;
import integrado.prog2.interfaces.ValidableMail;
import integrado.prog2.services.Crud_Generico.CrudGenerico;

public class ServicioUsuario extends CrudGenerico<Usuario> implements ValidableMail {

    @Override
    protected void validarCreacion(Usuario usuario) {
        validarMailUnico(usuario.getMail());
    }

    @Override
    protected void validarActualizacion(Usuario existente, Usuario nuevaInfo) {
        validarMailUnico(nuevaInfo.getMail(), existente.getId());
    }

    @Override
    protected void aplicarCambios(Usuario existente, Usuario nuevaInfo) {
        existente.setNombre(nuevaInfo.getNombre());
        existente.setApellido(nuevaInfo.getApellido());
        existente.setMail(nuevaInfo.getMail());
        existente.setCelular(nuevaInfo.getCelular());
        existente.setContrasenia(nuevaInfo.getContrasenia());
        existente.setRol(nuevaInfo.getRol());
    }

    @Override
    public void validarMailUnico(String mail) {
        validarMailUnico(mail, null);
    }

    @Override
    public void validarMailUnico(String mail, Long idExcluir) {
        for (Usuario u : datos) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail)) {
                if (idExcluir == null || !u.getId().equals(idExcluir)) throw new MailDuplicadoException("El mail ya esta registrado.");          
            }
        }
    }
    
    public boolean existeUsuarioActivo(Long id) {
        return datos.stream().anyMatch(user -> user.getId().equals(id) && !user.isEliminado());
    }
}