package integrado.prog2.interfaces;

public interface ValidableMail {
    void validarMailUnico(String mail);
    void validarMailUnico(String mail, Long idExcluir);
}