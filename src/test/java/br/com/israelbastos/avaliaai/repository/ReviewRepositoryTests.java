package br.com.israelbastos.avaliaai.repository;

import br.com.israelbastos.avaliaai.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("should ReviewRepository save review")
    void shouldReviewRepositorySaveReview() {

        Review review = new Review.Builder()
                .description("description")
                .build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    @DisplayName("should ReviewRepository find all reviews and return reviews")
    void shouldReviewRepositoryFindAllReviewsAndReturnReviews() {
        Review firstReview = new Review.Builder()
                .description("description")
                .build();

        Review secondReview = new Review.Builder()
                .description("description")
                .build();

        reviewRepository.save(firstReview);
        reviewRepository.save(secondReview);

        List<Review> reviews = reviewRepository.findAll();

        Assertions.assertThat(reviews).isNotNull();
        Assertions.assertThat(reviews.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should ReviewRepository find by id and return review")
    void shouldReviewRepositoryFindByIdAndReturnReview() {
        Review review = new Review.Builder()
                .description("description")
                .build();

        reviewRepository.save(review);

        Review found = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("should ReviewRepository find by description and return review not null")
    void shouldReviewRepositoryFindByDescriptionAndReturnReviewNotNull() {
        Review review = new Review.Builder()
                .description("description")
                .build();

        reviewRepository.save(review);

        Review found = reviewRepository.findByDescription(review.getDescription()).get();

        Assertions.assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("should ReviewRepository update review and return review not null")
    void shouldReviewRepositoryUpdateReviewAndReturnReviewNotNull() {
        Review review = new Review.Builder()
                .description("description")
                .build();

        reviewRepository.save(review);

        Review savedReview = reviewRepository.findById(review.getId()).get();
        savedReview.setDescription("Electric");

        Review updatedReview = reviewRepository.save(savedReview);

        Assertions.assertThat(updatedReview.getDescription()).isNotNull();
    }

    @Test
    @DisplayName("should ReviewRepository delete by id and return review is empty")
    void shouldReviewRepositoryDeleteByIdAndReturnReviewIsEmpty() {
        Review review = new Review.Builder()
                .description("description")
                .build();

        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());
        Optional<Review> returnedReview = reviewRepository.findById(review.getId());

        Assertions.assertThat(returnedReview).isEmpty();
    }
}
