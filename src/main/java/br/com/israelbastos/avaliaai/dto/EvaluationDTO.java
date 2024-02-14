package br.com.israelbastos.avaliaai.dto;

public record EvaluationDTO(
        long id,

        String title,

        String content,

        int stars) {

    public static class Builder {
        private long id;
        private String title;
        private String content;
        private int stars;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder stars(int stars) {
            this.stars = stars;
            return this;
        }

        public EvaluationDTO build() {
            return new EvaluationDTO(id, title, content, stars);
        }
    }
}
