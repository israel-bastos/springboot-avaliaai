package br.com.israelbastos.avaliaai.service;

import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.dto.ReviewResponseDTO;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.exception.ReviewNotFoundException;
import br.com.israelbastos.avaliaai.repository.ReviewRepository;
import br.com.israelbastos.avaliaai.service.implementation.ReviewServiceImplementation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    private Review review;

    private ReviewDTO reviewDTO;

    @BeforeEach
    void init() {
        review = new Review.Builder().id(1L).description("description").build();
        reviewDTO = new ReviewDTO.Builder().id(1L).description("description").build();
    }

    @Test
    @DisplayName("should review service create review and returns ReviewDTO")
    void shouldReviewServiceCreateReviewAndReturnsReviewDTO() {

        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDTO savedReviewDTO = reviewService.createReview(reviewDTO);

        Assertions.assertThat(savedReviewDTO).isNotNull();
    }

    @Test
    @DisplayName("should review service find all reviews and returns ReviewResponseDTO")
    void shouldReviewServiceGetAllReviewsAndReturnsReviewResponseDTO() {
        Page<Review> reviews = Mockito.mock(Page.class);

        when(reviewRepository.findAll(Mockito.any(Pageable.class))).thenReturn(reviews);

        ReviewResponseDTO savedReviewResponseDTO = reviewService.getAllReviews(1,10);

        Assertions.assertThat(savedReviewResponseDTO).isNotNull();
    }

    @Test
    @DisplayName("should review service find review by id and returns ReviewDTO")
    void shouldReviewServiceFindByIdAndReturnReviewDTO() {
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));

        ReviewDTO reviewReturned = reviewService.getReviewById(reviewId);

        Assertions.assertThat(reviewReturned).isNotNull();
    }

    @Test
    @DisplayName("should review service find review by id and returns ReviewNotFoundException")
    void shouldReviewServiceFindByIdAndReturnReviewNotFoundException() {
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> reviewService.getReviewById(reviewId))
                .withMessageContaining("Review could not be found");
    }

    @Test
    @DisplayName("should review service update review and returns ReviewDTO")
    void shouldReviewServiceUpdateReviewAndReturnReviewDTO() {
        long reviewId = 1;

        ReviewDTO reviewDTO = new ReviewDTO.Builder()
                .description("description updated")
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDTO updateReturned = reviewService.updateReview(reviewDTO, reviewId);

        Assertions.assertThat(updateReturned).isNotNull();
    }

    @Test
    @DisplayName("should review service update review and returns ReviewNotFoundException")
    void shouldReviewServiceUpdateReviewAndReturnReviewNotFoundException() {
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> reviewService.updateReview(reviewDTO, reviewId))
                .withMessageContaining("Review could not be updated");

    }

    @Test
    @DisplayName("should review service delete review by id and return void")
    void shouldReviewServiceDeleteReviewByIdAndReturnVoid() {
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));
        doNothing().when(reviewRepository).delete(review);

        assertAll(() -> reviewService.deleteReviewId(reviewId));
    }

    @Test
    @DisplayName("should review service delete review by id and return ReviewNotFoundException")
    void shouldReviewServiceDeleteReviewByIdAndReturnReviewNotFoundException() {
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> reviewService.deleteReviewId(reviewId))
                .withMessageContaining("Review could not be deleted");
    }
}
