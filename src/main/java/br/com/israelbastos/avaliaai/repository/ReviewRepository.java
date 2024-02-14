package br.com.israelbastos.avaliaai.repository;

import br.com.israelbastos.avaliaai.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByDescription(String description);
}
