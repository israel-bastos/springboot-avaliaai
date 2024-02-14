package br.com.israelbastos.avaliaai.dto;

public record ReviewDTO(
        long id,

        String description
) {
    public static class Builder {
        private long id;
        private String description;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ReviewDTO build() {
            return new ReviewDTO(id, description);
        }
    }
}
