package br.com.israelbastos.avaliaai.dto;

public record AuthResponseDTO(
        String accessToken,

        String tokenType,

        String error
) {
    public static AuthResponseDTO forSuccess(String accessToken) {
        return new AuthResponseDTO(accessToken, "Bearer ", null);
    }

    public static AuthResponseDTO forError(String error) {
        return new AuthResponseDTO(null, null, error);
    }
}
