package br.com.israelbastos.avaliaai.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

@RunWith(SpringRunner.class)
class EvaluationBadRequestExceptionHandlerTests {

    @Test
    @DisplayName("should return EvaluationBadRequestExceptionHandler with a specialized message when capturing any EvaluationBadRequestException")
    void shouldReturnEvaluationBadRequestExceptionHandlerWithASpecializedMessageWhenCapturingAnyEvaluationBadRequestException() {

        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        EvaluationBadRequestException evaluationBadRequestException = new EvaluationBadRequestException("Evaluation bad request with specialized message");

        ResponseEntity<ExceptionDetails> response = advice.handleEvaluationBadRequestException(evaluationBadRequestException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Evaluation bad request with specialized message");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("should return EvaluationBadRequestExceptionHandler with a default message when capturing any EvaluationBadRequestException")
    void shouldReturnEvaluationBadRequestExceptionHandlerWithADefaultMessageWhenCapturingAnyEvaluationBadRequestException() {

        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        EvaluationBadRequestException evaluationBadRequestException = new EvaluationBadRequestException();

        ResponseEntity<ExceptionDetails> response = advice.handleEvaluationBadRequestException(evaluationBadRequestException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Evaluation bad request");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
