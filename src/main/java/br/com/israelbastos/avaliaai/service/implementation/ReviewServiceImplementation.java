package br.com.israelbastos.avaliaai.service.implementation;

import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.dto.ReviewResponseDTO;
import br.com.israelbastos.avaliaai.entity.Review;
import br.com.israelbastos.avaliaai.exception.ReviewNotFoundException;
import br.com.israelbastos.avaliaai.repository.ReviewRepository;
import br.com.israelbastos.avaliaai.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository repository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReviewDTO createReview(ReviewDTO dto) {
        Review review = mapToEntity(dto);
        review.setDescription(dto.description());

        Review newReview = repository.save(review);

        return new ReviewDTO(
                newReview.getId(),
                newReview.getDescription()
        );
    }

    @Override
    public ReviewResponseDTO getAllReviews(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Review> reviews = repository.findAll(pageable);

        List<Review> listOfReview = reviews.getContent();
        List<ReviewDTO> content = listOfReview
                .stream()
                .map(this::mapToDto)
                .toList();

        return new ReviewResponseDTO(
                content,
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalElements(),
                reviews.getTotalPages(),
                reviews.isLast()
        );
    }

    @Override
    public ReviewDTO getReviewById(long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review could not be found"));

        return mapToDto(review);
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO dto, long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review could not be updated"));

        review.setDescription(dto.description());

        Review updatedReview = repository.save(review);

        return mapToDto(updatedReview);
    }

    @Override
    public void deleteReviewId(long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review could not be deleted"));

        repository.delete(review);
    }

    private ReviewDTO mapToDto(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getDescription()
        );
    }

    private Review mapToEntity(ReviewDTO dto) {
        Review review = new Review();
        review.setDescription(dto.description());

        return review;
    }
}
