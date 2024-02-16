package br.com.israelbastos.avaliaai.exception;

public class EvaluationBadRequestException extends RuntimeException {

    EvaluationBadRequestException(String message) { super(message); }

    public EvaluationBadRequestException() { super("Evaluation bad request"); }
}
