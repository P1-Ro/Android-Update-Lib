package sk.p1ro.updater.util;

public class UpdateException extends RuntimeException {
    public UpdateException(Exception e) {
        super(e);
    }

    public UpdateException(String e) {
        super(e);
    }
}
