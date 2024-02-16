package br.com.israelbastos.avaliaai.exception;

public class EvaluationNotFoundException extends RuntimeException {

    public EvaluationNotFoundException(String message) {
        super(message);
    }

    public EvaluationNotFoundException() { super("Evaluation not found"); }
}
