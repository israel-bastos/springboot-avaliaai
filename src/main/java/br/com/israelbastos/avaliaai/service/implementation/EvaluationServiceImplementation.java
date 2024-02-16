package br.com.israelbastos.avaliaai.service.implementation;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import br.com.israelbastos.avaliaai.entity.Evaluation;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.exception.EvaluationNotFoundException;
import br.com.israelbastos.avaliaai.exception.ReviewNotFoundException;
import br.com.israelbastos.avaliaai.repository.EvaluationRepository;
import br.com.israelbastos.avaliaai.repository.ReviewRepository;
import br.com.israelbastos.avaliaai.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImplementation implements EvaluationService {

    private final ReviewRepository reviewRepository;

    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EvaluationServiceImplementation(ReviewRepository reviewRepository, EvaluationRepository evaluationRepository) {
        this.reviewRepository = reviewRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public EvaluationDTO createEvaluation(long reviewId, EvaluationDTO dto) {
        Evaluation evaluation = mapToEntity(dto);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Evaluation with associated review not found"));

        evaluation.setReview(review);

        Evaluation newEvaluation = evaluationRepository.save(evaluation);

        return mapToDto(newEvaluation);
    }

    @Override
    public List<EvaluationDTO> findEvaluationsByReviewId(long id) {
        List<Evaluation> evaluations = evaluationRepository.findEvaluationsByReviewId(id);

        if (evaluations.isEmpty()) {
            throw new EvaluationNotFoundException("No evaluations found for this review");
        }

        return evaluations.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public EvaluationDTO findEvaluationById(long evaluationId, long reviewId) {
        Review review = reviewRepository
                .findById(evaluationId).orElseThrow(() -> new ReviewNotFoundException("Evaluation with associated review not found"));

        Evaluation evaluation = evaluationRepository
                .findById(evaluationId).orElseThrow(() -> new EvaluationNotFoundException("Review with associate evaluation not found"));

        if(evaluation.getReview().getId() != review.getId()) {
            throw new EvaluationNotFoundException("This review does not belong to a evaluation");
        }

        return mapToDto(evaluation);
    }


    @Override
    public EvaluationDTO updateEvaluation(long reviewId, long evaluationId, EvaluationDTO dto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Evaluation with associated review not found"));

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new EvaluationNotFoundException("Review with associate evaluation not found"));

        if(evaluation.getReview().getId() != review.getId()) {
            throw new EvaluationNotFoundException("This review does not belong to a evaluation");
        }

        evaluation.setTitle(dto.title());
        evaluation.setContent(dto.content());
        evaluation.setStars(dto.stars());
        evaluation.setReview(review);

        Evaluation updateEvaluation = evaluationRepository.save(evaluation);

        return mapToDto(updateEvaluation);
    }

    @Override
    public void deleteEvaluation(long reviewId, long evaluationId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Evaluation with associated review not found"));

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new EvaluationNotFoundException("Review with associate evaluation not found"));

        if(evaluation.getReview().getId() != review.getId()) {
            throw new EvaluationNotFoundException("This review does not belong to a evaluation");
        }

        evaluationRepository.delete(evaluation);
    }

    private EvaluationDTO mapToDto(Evaluation evaluation) {
        return new EvaluationDTO(
                evaluation.getId(),
                evaluation.getTitle(),
                evaluation.getContent(),
                evaluation.getStars()
        );
    }

    private Evaluation mapToEntity(EvaluationDTO dto) {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(dto.id());
        evaluation.setTitle(dto.title());
        evaluation.setContent(dto.content());
        evaluation.setStars(dto.stars());

        return evaluation;
    }
}
