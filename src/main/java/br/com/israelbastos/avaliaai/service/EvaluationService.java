package br.com.israelbastos.avaliaai.service;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EvaluationService {

    EvaluationDTO createEvaluation(long reviewId, EvaluationDTO dto);

    List<EvaluationDTO> findEvaluationsByReviewId(long id);

    EvaluationDTO findEvaluationById(long evaluationId, long reviewId);

    EvaluationDTO updateEvaluation(long reviewId, long evaluationId, EvaluationDTO dto);

    void deleteEvaluation(long reviewId, long evaluationId);
}
