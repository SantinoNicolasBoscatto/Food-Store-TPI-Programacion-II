package integrado.prog2.exceptions;

public class MailDuplicadoException extends RuntimeException {
    public MailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}