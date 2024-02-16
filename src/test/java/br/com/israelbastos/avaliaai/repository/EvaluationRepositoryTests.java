package br.com.israelbastos.avaliaai.repository;

import br.com.israelbastos.avaliaai.entity.Evaluation;
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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EvaluationRepositoryTests {
    private final EvaluationRepository evaluationRepository;

    @Autowired
    EvaluationRepositoryTests(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @Test
    @DisplayName("should EvaluationRepository save and returns evaluation")
    void shouldEvaluationRepositorySaveAndReturnsEvaluation() {
        Evaluation evaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        Assertions.assertThat(savedEvaluation).isNotNull();
        Assertions.assertThat(savedEvaluation.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should EvaluationRepository find all and returns evaluations")
    void shouldEvaluationRepositoryFindAllAndReturnsEvaluations() {
        Evaluation firstEvaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();
        Evaluation secondEvaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();

        evaluationRepository.save(firstEvaluation);
        evaluationRepository.save(secondEvaluation);

        List<Evaluation> evaluations = evaluationRepository.findAll();

        Assertions.assertThat(evaluations).isNotNull();
        Assertions.assertThat(evaluations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should EvaluationRepository find by id and returns evaluation")
    void shouldEvaluationRepositoryFindByIdAndReturnsEvaluation() {
        Evaluation evaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();

        evaluationRepository.save(evaluation);

        Evaluation returnedEvaluation = evaluationRepository.findById(evaluation.getId()).get();

        Assertions.assertThat(returnedEvaluation).isNotNull();
    }

    @Test
    @DisplayName("should EvaluationRepository update evaluation and returns evaluation")
    void shouldEvaluationRepositoryUpdateEvaluationAndReturnsEvaluation() {
        Evaluation evaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();

        evaluationRepository.save(evaluation);

        Evaluation returnedEvaluation = evaluationRepository.findById(evaluation.getId()).get();
        returnedEvaluation.setTitle("title");
        returnedEvaluation.setContent("content");
        Evaluation udpatedEvaluation = evaluationRepository.save(returnedEvaluation);

        Assertions.assertThat(udpatedEvaluation.getTitle()).isNotNull();
        Assertions.assertThat(udpatedEvaluation.getContent()).isNotNull();
    }

    @Test
    @DisplayName("should EvaluationRepository delete by id and returns evaluation empty")
    void shouldEvaluationRepositoryDeleteByIdAndReturnsEvaluationEmpty() {
        Evaluation evaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();

        evaluationRepository.save(evaluation);

        evaluationRepository.deleteById(evaluation.getId());
        Optional<Evaluation> returnedEvaluation = evaluationRepository.findById(evaluation.getId());

        Assertions.assertThat(returnedEvaluation).isEmpty();
    }
}
