package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.ReviewDTO;
import br.com.israelbastos.avaliaai.dto.ReviewResponseDTO;
import br.com.israelbastos.avaliaai.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("reviews")
    public ResponseEntity<ReviewResponseDTO> getReviews(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(reviewService.getAllReviews(pageNumber, pageSize));
    }

    @GetMapping("reviews/{id}")
    public ResponseEntity<ReviewDTO> reviewDetail(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping("reviews/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO dto) {
        return new ResponseEntity<>(reviewService.createReview(dto), HttpStatus.CREATED);
    }

    @PutMapping("reviews/{id}/update")
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO dto, @PathVariable("id") int id) {
        ReviewDTO response = reviewService.updateReview(dto, id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("reviews/{id}/delete")
    public ResponseEntity<String> deleteReview(@PathVariable("id") int id) {
        reviewService.deleteReviewId(id);

        return ResponseEntity.ok("Review deleted");
    }
}
