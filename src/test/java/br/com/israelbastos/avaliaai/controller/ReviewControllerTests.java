package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.EvaluationDTO;
import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.dto.ReviewResponseDTO;
import br.com.israelbastos.avaliaai.entity.Evaluation;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class ReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

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
    @DisplayName("should review controller create evaluation and returns created")
    void shouldReviewControllerCreateEvaluationAndReturnCreated() throws Exception {
        when(reviewService.createReview(ArgumentMatchers.any()))
                .thenAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/v1/reviews/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(reviewDTO.description())));
    }

    @Test
    @DisplayName("should review controller get all reviews and returns review response dto")
    void shouldReviewControllerGetAllReviewsAndReturnsReviewResponseDTO() throws Exception {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO.Builder()
                .content(Collections.singletonList(reviewDTO))
                .pageNumber(1)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();

        when(reviewService.getAllReviews(1,10)).thenReturn(reviewResponseDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber","1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.content.size()",
                        CoreMatchers.is(reviewResponseDTO.content().size())));
    }

    @Test
    @DisplayName("should review controller get review detail and returns review dto")
    void shouldReviewControllerGetReviewDetailAndReturnsReviewDTO() throws Exception {
        int reviewId = 1;
        when(reviewService.getReviewById(reviewId)).thenReturn(reviewDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(reviewDTO.description())));
    }

    @Test
    @DisplayName("should review controller update review and returns review dto")
    void shouldReviewControllerUpdateReviewAndReturnsReviewDTO() throws Exception {
        int reviewId = 1;
        when(reviewService.updateReview(reviewDTO, reviewId)).thenReturn(reviewDTO);

        ResultActions response = mockMvc.perform(put("/api/v1/reviews/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(reviewDTO.description())));
    }

    @Test
    @DisplayName("should review controller delete review and returns ok")
    void shouldReviewControllerDeleteReviewAndReturnsOk() throws Exception {
        int reviewId = 1;
        doNothing().when(reviewService).deleteReviewId(reviewId);

        ResultActions response = mockMvc.perform(delete("/api/v1/reviews/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
