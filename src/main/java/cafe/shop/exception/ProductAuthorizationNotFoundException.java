package cafe.shop.exception;

public class ProductAuthorizationNotFoundException extends RuntimeException {

    public ProductAuthorizationNotFoundException(String message) {
        super(message);
    }
}
