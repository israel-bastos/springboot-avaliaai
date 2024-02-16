package br.com.israelbastos.avaliaai.security;

public final class SecurityConstants {
    public static final long JWT_EXPIRATION = 70000;

    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }
}
