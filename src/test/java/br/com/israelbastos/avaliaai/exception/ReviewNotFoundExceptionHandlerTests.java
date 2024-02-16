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
class ReviewNotFoundExceptionHandlerTests {

    @Test
    @DisplayName("should return ReviewNotFoundExceptionHandler with a specialized message when capturing any ReviewNotFoundException")
    void shouldReturnReviewNotFoundExceptionHandlerWithASpecializedMessageWhenCapturingAnyReviewNotFoundException() {

        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        ReviewNotFoundException reviewNotFoundException = new ReviewNotFoundException("Review not found specialized message");

        ResponseEntity<ExceptionDetails> response = advice.handleReviewNotFoundException(reviewNotFoundException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Review not found specialized message");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("should return ReviewNotFoundExceptionHandler with a default message when capturing any ReviewNotFoundException")
    void shouldReturnReviewNotFoundExceptionHandlerWithADefaultMessageWhenCapturingAnyReviewNotFoundException() {
        GlobalExceptionHandler advice = new GlobalExceptionHandler();
        ReviewNotFoundException reviewNotFoundException = new ReviewNotFoundException();

        ResponseEntity<ExceptionDetails> response = advice.handleReviewNotFoundException(reviewNotFoundException, null);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().message()).isEqualTo("Review not found");
        Assertions.assertThat(response.getBody().timestamp().toInstant()).isBefore(Instant.now());
        Assertions.assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
