package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import br.com.israelbastos.avaliaai.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/reviews/{reviewId}/evaluations")
    public ResponseEntity<EvaluationDTO> createEvaluation(@PathVariable(value = "reviewId") int reviewId, @RequestBody EvaluationDTO dto) {
        return new ResponseEntity<>(evaluationService.createEvaluation(reviewId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/reviews/{reviewId}/evaluations")
    public List<EvaluationDTO> getEvaluationByEvaluationId(@PathVariable(value = "reviewId") int reviewId) {
        return evaluationService.findEvaluationsByReviewId(reviewId);
    }

    @GetMapping("/reviews/{reviewId}/evaluations/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable(value = "reviewId") int reviewId, @PathVariable(value = "id") int evaluationId) {
        EvaluationDTO reviewDto = evaluationService.findEvaluationById(reviewId, evaluationId);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @PutMapping("/reviews/{reviewId}/evaluations/{id}/update")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@PathVariable(value = "reviewId") int reviewId,
                                                      @PathVariable(value = "id") int evaluationId,
                                                      @RequestBody EvaluationDTO dto) {
        EvaluationDTO updatedEvaluation = evaluationService.updateEvaluation(reviewId, evaluationId, dto);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}/evaluations/{id}/delete")
    public ResponseEntity<String> deleteEvaluation(@PathVariable(value = "reviewId") int reviewId,
                                                   @PathVariable(value = "id") int evaluationId) {
        evaluationService.deleteEvaluation(reviewId, evaluationId);
        return new ResponseEntity<>("Evaluation deleted successfully", HttpStatus.OK);
    }
}
