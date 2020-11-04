package exceptions;

public class AanmaakGebruikerAfgebrokenException extends RuntimeException {
    public AanmaakGebruikerAfgebrokenException(){
        super("Aanmelden afgebroken door de gebruiker.");
    }
}
