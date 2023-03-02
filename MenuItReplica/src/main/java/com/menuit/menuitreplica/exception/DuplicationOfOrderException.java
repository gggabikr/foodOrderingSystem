package com.menuit.menuitreplica.exception;

public class DuplicationOfOrderException extends RuntimeException{
    public DuplicationOfOrderException() {
        super();
    }

    public DuplicationOfOrderException(String message) {
        super(message);
    }

    public DuplicationOfOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationOfOrderException(Throwable cause) {
        super(cause);
    }
}
