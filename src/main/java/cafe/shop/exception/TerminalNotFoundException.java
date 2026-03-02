package cafe.shop.exception;

public class TerminalNotFoundException extends RuntimeException {

    public TerminalNotFoundException(String message) {
        super(message);
    }
}
