package br.com.israelbastos.avaliaai.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException() { super("Review not found" );}
}
