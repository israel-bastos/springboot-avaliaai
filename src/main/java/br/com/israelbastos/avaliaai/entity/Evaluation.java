package br.com.israelbastos.avaliaai.entity;

import javax.persistence.*;
@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    private int stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public Evaluation() {}

    public Evaluation(long id, String title, String content, int stars, Review review) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.stars = stars;
        this.review = review;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public static class Builder {
        private long id;

        private String title;

        private String content;

        private int stars;

        private Review review;

        public Evaluation.Builder id(long id) {
            this.id = id;
            return this;
        }

        public Evaluation.Builder title(String title) {
            this.title = title;
            return this;
        }

        public Evaluation.Builder content(String content) {
            this.content = content;
            return this;
        }

        public Evaluation.Builder stars(int stars) {
            this.stars = stars;
            return this;
        }

        public Evaluation.Builder review(Review review) {
            this.review = review;
            return this;
        }

        public Evaluation build() {
            return new Evaluation(id, title, content, stars, review);
        }
    }
}
