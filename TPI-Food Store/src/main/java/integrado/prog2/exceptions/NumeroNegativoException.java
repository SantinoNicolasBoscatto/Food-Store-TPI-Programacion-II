package integrado.prog2.exceptions;

public class NumeroNegativoException extends RuntimeException {
    public NumeroNegativoException(String mensaje) {
        super(mensaje);
    }
}