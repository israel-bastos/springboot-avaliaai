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
import java.util.Date;

@RunWith(SpringRunner.class)
class ReviewBadRequestExceptionHandlerTests {

    @Test
    @DisplayName("should return ReviewBadRequestExceptionHandler with a specialized message when capturing any ReviewBadRequestException")
    void shouldReturnReviewBadRequestExceptionHandlerWithASpecializedMessageWhenCapturingAnyReviewBadRequestException() {

        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        ReviewBadRequestException reviewNotFoundException = new ReviewBadRequestException("Review bad request with specialized message");

        ResponseEntity<ExceptionDetails> response = advice.handleReviewBadRequestException(reviewNotFoundException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Review bad request with specialized message");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("should return ReviewBadRequestExceptionHandler with a default message when capturing any ReviewBadRequestException")
    void shouldReturnReviewBadRequestExceptionHandlerWithADefaultMessageWhenCapturingAnyReviewBadRequestException() {

        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        ReviewBadRequestException reviewBadRequestException = new ReviewBadRequestException();

        ResponseEntity<ExceptionDetails> response = advice.handleReviewBadRequestException(reviewBadRequestException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Review bad request");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
