package br.com.israelbastos.avaliaai.dto;

import java.util.List;

public record ReviewResponseDTO(
        List<ReviewDTO> content,

        int pageNumber,

        int pageSize,

        long totalElements,

        int totalPages,

        boolean last
) {
    public static class Builder {
        private List<ReviewDTO> content;

        private int pageNumber;

        private int pageSize;

        private long totalElements;

        private int totalPages;

        private boolean last;

        public Builder content(List<ReviewDTO> content) {
            this.content = content;
            return this;
        }

        public Builder pageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder last(boolean last) {
            this.last = last;
            return this;
        }

        public ReviewResponseDTO build() {
            return new ReviewResponseDTO(content, pageNumber, pageSize, totalElements, totalPages, last);
        }
    }
}
