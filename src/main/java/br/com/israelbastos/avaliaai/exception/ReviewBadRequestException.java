package br.com.israelbastos.avaliaai.exception;

public class ReviewBadRequestException extends RuntimeException {

    public ReviewBadRequestException(String message) {
        super(message);
    }

    public ReviewBadRequestException() { super("Review bad request"); }
}
