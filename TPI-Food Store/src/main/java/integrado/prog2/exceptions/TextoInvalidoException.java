package integrado.prog2.exceptions;

public class TextoInvalidoException extends RuntimeException {
    public TextoInvalidoException(String mensaje) {
        super(mensaje);
    }
}