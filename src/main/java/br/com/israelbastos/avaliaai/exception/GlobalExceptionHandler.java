package br.com.israelbastos.avaliaai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleReviewNotFoundException(ReviewNotFoundException ex,
                                                                          WebRequest request) {

        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewBadRequestException.class)
    public ResponseEntity<ExceptionDetails> handleReviewBadRequestException(ReviewBadRequestException ex,
                                                                          WebRequest request) {

        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EvaluationNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEvaluationNotFoundException(EvaluationNotFoundException ex,
                                                                              WebRequest request) {

        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EvaluationBadRequestException.class)
    public ResponseEntity<ExceptionDetails> handleEvaluationBadRequestException(EvaluationBadRequestException ex,
                                                                              WebRequest request) {

        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }
}
