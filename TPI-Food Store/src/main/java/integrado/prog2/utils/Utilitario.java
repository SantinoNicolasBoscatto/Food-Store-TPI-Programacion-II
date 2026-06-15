package integrado.prog2.utils;
import integrado.prog2.exceptions.NumeroNegativoException;
import integrado.prog2.exceptions.NumeroNoPositivoException;
import integrado.prog2.exceptions.TextoInvalidoException;
import integrado.prog2.exceptions.ValidacionException;

public class Utilitario {
    public static void validarTextoNoVacio(String texto, String nombreCampo) throws TextoInvalidoException {
        if (texto == null || texto.isBlank()) throw new TextoInvalidoException("El campo " + nombreCampo + " no puede ser nulo ni estar vacio.");      
    }

    public static void validarPositivo(double valor, String nombreCampo) throws NumeroNoPositivoException {
        if (valor <= 0) throw new NumeroNoPositivoException("El campo " + nombreCampo + " debe ser un valor mayor a cero.");    
    }

    public static void validarNoNegativo(double valor, String nombreCampo) throws  NumeroNegativoException{
        if (valor < 0) throw new NumeroNegativoException("El campo " + nombreCampo + " no puede ser un valor negativo.");      
    }
    
    public static void validarFormatoMail(String mail, String nombreCampo) {
        if (mail == null || !mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) 
            throw new ValidacionException("El campo " + nombreCampo + " debe tener un formato de mail valido.");
        
    }
}
