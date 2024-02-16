package br.com.israelbastos.avaliaai.service;

import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.dto.ReviewResponseDTO;

public interface ReviewService {
    ReviewDTO createReview(ReviewDTO dto);

    ReviewResponseDTO getAllReviews(int pageNo, int pageSize);

    ReviewDTO getReviewById(long id);

    ReviewDTO updateReview(ReviewDTO dto, long id);

    void deleteReviewId(long id);
}
