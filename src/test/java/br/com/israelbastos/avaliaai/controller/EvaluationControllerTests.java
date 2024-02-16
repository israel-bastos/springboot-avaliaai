package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.entity.Evaluation;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.service.EvaluationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = EvaluationController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class EvaluationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EvaluationService evaluationService;

    private Review review;

    private Evaluation evaluation;

    private EvaluationDTO evaluationDTO;

    private ReviewDTO reviewDTO;

    @BeforeEach
    void init() {
        review = new Review.Builder().description("description").build();
        evaluation = new Evaluation.Builder().title("title").content("content").stars(5).build();
        reviewDTO = new ReviewDTO.Builder().description("description").build();
        evaluationDTO = new EvaluationDTO.Builder().title("review title").content("test content").stars(5).build();
    }

    @Test
    @DisplayName("should evaluation controller get evaluation by review id and returns evaluation dto")
    void shouldEvaluationControllerGetEvaluationByReviewIdAndReturnsEvaluationDTO() throws Exception {
        int reviewId = 1;
        when(evaluationService.findEvaluationsByReviewId(reviewId))
                .thenReturn(Collections.singletonList(evaluationDTO));

        ResultActions response = mockMvc.perform(get("/api/v1/reviews/1/evaluations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", CoreMatchers.is(Collections.singletonList(evaluationDTO).size())));
    }

    @Test
    @DisplayName("should evaluation controller update evaluation and returns evaluation dto")
    void shouldEvaluationControllerUpdateEvaluationAndReturnsEvaluationDTO() throws Exception {
        int reviewId = 1;
        int evaluationId = 1;
        when(evaluationService.updateEvaluation(reviewId, evaluationId, evaluationDTO)).thenReturn(evaluationDTO);

        ResultActions response = mockMvc.perform(put("/api/v1/reviews/1/evaluations/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluationDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(evaluationDTO.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(evaluationDTO.content())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(evaluationDTO.stars())));
    }

    @Test
    @DisplayName("should evaluation controller create evaluation and returns evaluation dto")
    void shouldEvaluationControllerCreateEvaluationAndReturnsEvaluationDTO() throws Exception {
        int reviewId = 1;
        when(evaluationService.createEvaluation(reviewId, evaluationDTO)).thenReturn(evaluationDTO);

        ResultActions response = mockMvc.perform(post("/api/v1/reviews/1/evaluations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluationDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(evaluationDTO.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(evaluationDTO.content())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(evaluationDTO.stars())));
    }

    @Test
    @DisplayName("should evaluation controller get evaluation by id and returns evaluation dto")
    void shouldEvaluationControllerGetEvaluationByIdAndReturnsEvaluationDTO() throws Exception {
        int reviewId = 1;
        int evaluationId = 1;
        when(evaluationService.findEvaluationById(evaluationId, reviewId)).thenReturn(evaluationDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/reviews/1/evaluations/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(evaluationDTO.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(evaluationDTO.content())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(evaluationDTO.stars())));
    }

    @Test
    @DisplayName("should evaluation controller delete evaluation and returns ok")
    void shouldEvaluationControllerDeleteEvaluationAndReturnsOk() throws Exception {
        int reviewId = 1;
        int evaluationId = 1;

        doNothing().when(evaluationService).deleteEvaluation(reviewId, evaluationId);

        ResultActions response = mockMvc.perform(delete("/api/v1/reviews/1/evaluations/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
