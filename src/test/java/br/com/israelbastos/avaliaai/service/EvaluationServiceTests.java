package br.com.israelbastos.avaliaai.service;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.entity.Evaluation;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.exception.EvaluationNotFoundException;
import br.com.israelbastos.avaliaai.exception.ReviewNotFoundException;
import br.com.israelbastos.avaliaai.repository.EvaluationRepository;
import br.com.israelbastos.avaliaai.repository.ReviewRepository;
import br.com.israelbastos.avaliaai.service.implementation.EvaluationServiceImplementation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTests {

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private EvaluationServiceImplementation evaluationService;

    private Review review;

    private Evaluation evaluation;

    private EvaluationDTO evaluationDTO;

    private ReviewDTO reviewDTO;

    @BeforeEach
    void init() {
        review = new Review.Builder().id(1L).description("description").build();
        evaluation = new Evaluation.Builder().id(1L).review(review).title("title").content("content").stars(5).build();
        reviewDTO = new ReviewDTO.Builder().id(1L).description("description").build();
        evaluationDTO = new EvaluationDTO.Builder().id(1L).title("review title").content("test content").stars(5).build();
    }

    @Test
    @DisplayName("should evaluation service create evaluation and returns EvaluationDTO")
    void shouldEvaluationServiceCreateEvaluationAndReturnsEvaluationDTO() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(evaluationRepository.save(Mockito.any(Evaluation.class))).thenReturn(evaluation);

        EvaluationDTO savedEvaluation = evaluationService.createEvaluation(review.getId(), evaluationDTO);

        Assertions.assertThat(savedEvaluation).isNotNull();
    }

    @Test
    @DisplayName("should evaluation service create evaluation and returns ReviewNotFoundException")
    void shouldEvaluationServiceCreateEvaluationAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> evaluationService.createEvaluation(reviewId, evaluationDTO))
                .withMessageContaining("Evaluation with associated review not found");
    }

    @Test
    @DisplayName("should evaluation service find evaluations by reviewId and returns EvaluationDTO")
    void shouldEvaluationServiceFindEvaluationsByReviewIdAndReturnsEvaluationDTO() {
        int evaluationId = 1;

        when(evaluationRepository.findEvaluationsByReviewId(evaluationId)).thenReturn(Collections.singletonList(evaluation));

        List<EvaluationDTO> reviewReturn = evaluationService.findEvaluationsByReviewId(evaluationId);

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    @DisplayName("should evaluation service find evaluations by reviewId and returns empty list and EvaluationNotFoundException")
    void shouldEvaluationServiceFindByIdAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;

        when(evaluationRepository.findEvaluationsByReviewId(reviewId)).thenReturn(Collections.emptyList());

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.findEvaluationsByReviewId(reviewId))
                .withMessageContaining("No evaluations found for this review");
    }

    @Test
    @DisplayName("should evaluation service find evaluation by id and returns EvaluationDTO")
    void shouldEvaluationServiceFindEvaluationByIdAndReturnsEvaluationDTO() {
        long evaluationId = 1;
        long reviewId = 1;

        evaluation.setReview(review);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        EvaluationDTO evaluationReturn = evaluationService.findEvaluationById(evaluationId, reviewId);

        Assertions.assertThat(evaluationReturn).isNotNull();
    }

    @Test
    @DisplayName("should evaluation service find evaluation by reviewId and returns ReviewNotFoundException")
    void shouldReviewServiceFindByIdAndReturnsReviewNotFoundExceptions() {
        long evaluationId = 1;
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> evaluationService.findEvaluationById(evaluationId, reviewId))
                .withMessageContaining("Evaluation with associated review not found");
    }

    @Test
    @DisplayName("should evaluation service find evaluation by reviewId and returns EvaluationNotFoundException")
    void shouldReviewServiceFindByIdAndReturnsEvaluationNotFoundException() {
        long evaluationId = 1;
        long reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.findEvaluationById(evaluationId, reviewId))
                .withMessageContaining("Review with associate evaluation not found");
    }

    @Test
    @DisplayName("should evaluation service do not find evaluation by reviewId and returns EvaluationNotFoundException")
    void shouldReviewServiceFindByIdAndReviewDoesNotBelongToEvaluationAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        Review wrongReview = new Review.Builder().id(666L).description("description").build();
        evaluation.setReview(wrongReview);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.findEvaluationById(evaluationId, reviewId))
                .withMessageContaining("This review does not belong to a evaluation");
    }

    @Test
    @DisplayName("should evaluation service update review and returns EvaluationDTO")
    void shouldEvaluationServiceUpdateReviewAndReturnsEvaluationDTO() {
        long reviewId = 1;
        long evaluationId = 1;

        review.setEvaluations(Collections.singletonList(evaluation));
        evaluation.setReview(review);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(evaluation)).thenReturn(evaluation);

        EvaluationDTO updatedReturn = evaluationService.updateEvaluation(reviewId, evaluationId, evaluationDTO);

        Assertions.assertThat(updatedReturn).isNotNull();
    }

    @Test
    @DisplayName("should evaluation service update review and returns ReviewNotFoundException")
    void shouldEvaluationServiceUpdateReviewAndReturnsReviewNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> evaluationService.updateEvaluation(reviewId, evaluationId, evaluationDTO))
                .withMessageContaining("Evaluation with associated review not found");
    }

    @Test
    @DisplayName("should evaluation service update review and returns EvaluationNotFoundException")
    void shouldEvaluationServiceUpdateReviewAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.updateEvaluation(reviewId, evaluationId, evaluationDTO))
                .withMessageContaining("Review with associate evaluation not found");
    }

    @Test
    @DisplayName("should evaluation service do not update review and returns EvaluationNotFoundException")
    void shouldEvaluationServiceUpdateAndReviewDoesNotBelongToEvaluationAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        Review wrongReview = new Review.Builder().id(666L).description("description").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(wrongReview));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.updateEvaluation(reviewId, evaluationId, evaluationDTO))
                .withMessageContaining("This review does not belong to a evaluation");
    }

    @Test
    @DisplayName("should evaluation service delete review by id and returns Void")
    void shouldEvaluationServiceDeleteReviewByIdAndReturnsVoid() {
        long evaluationId = 1;
        long reviewId = 1;

        review.setEvaluations(Collections.singletonList(evaluation));
        evaluation.setReview(review);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        assertAll(() -> evaluationService.deleteEvaluation(reviewId, evaluationId));
    }

    @Test
    @DisplayName("should evaluation service delete review by id and returns ReviewNotFoundException")
    void shouldEvaluationServiceDeleteReviewAndReturnsReviewNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ReviewNotFoundException.class)
                .isThrownBy(() -> evaluationService.deleteEvaluation(reviewId, evaluationId))
                .withMessageContaining("Evaluation with associated review not found");
    }

    @Test
    @DisplayName("should evaluation service delete review by id and returns EvaluationNotFoundException")
    void shouldEvaluationServiceDeleteReviewAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.deleteEvaluation(reviewId, evaluationId))
                .withMessageContaining("Review with associate evaluation not found");
    }

    @Test
    @DisplayName("should evaluation service do not delete review and returns EvaluationNotFoundException")
    void shouldEvaluationServiceDeleteAndReviewDoesNotBelongToEvaluationAndReturnsEvaluationNotFoundException() {
        long reviewId = 1;
        long evaluationId = 1;

        Review wrongReview = new Review.Builder().id(666L).description("description").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(wrongReview));
        when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        Assertions.assertThatExceptionOfType(EvaluationNotFoundException.class)
                .isThrownBy(() -> evaluationService.deleteEvaluation(reviewId, evaluationId))
                .withMessageContaining("This review does not belong to a evaluation");
    }
}
