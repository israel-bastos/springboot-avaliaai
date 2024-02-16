package br.com.israelbastos.avaliaai.repository;

import br.com.israelbastos.avaliaai.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {


    List<Evaluation> findEvaluationsByReviewId(long reviewId);
}
