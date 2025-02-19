package exceptions;

public class BrowserNotSupportedException extends RuntimeException {
    public BrowserNotSupportedException(String exception) {
        super(String.format("Browser %s not supported", exception));
    }
}