package desafio.exception;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(String msg) {
        super(msg);
    }
}
