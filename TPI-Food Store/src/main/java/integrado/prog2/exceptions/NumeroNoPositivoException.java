package integrado.prog2.exceptions;

public class NumeroNoPositivoException extends RuntimeException {
    public NumeroNoPositivoException(String mensaje) {
        super(mensaje);
    }
}