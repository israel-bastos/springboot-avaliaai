package br.com.israelbastos.avaliaai.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public Review() {}

    public Review(long id, String description, List<Evaluation> evaluations) {
        this.id = id;
        this.description = description;
        this.evaluations = evaluations;
    }

    public static class Builder {
        private long id;

        private String description;

        private List<Evaluation> evaluations = new ArrayList<>();

        public Review.Builder id(long id) {
            this.id = id;
            return this;
        }

        public Review.Builder description(String description) {
            this.description = description;
            return this;
        }

        public Review.Builder evaluations(List<Evaluation> evaluations) {
            this.evaluations = evaluations;
            return this;
        }

        public Review build() {
            return new Review(id, description, evaluations);
        }
    }
}
