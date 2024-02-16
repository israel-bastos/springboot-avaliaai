package br.com.israelbastos.avaliaai.exception;

import java.util.Date;

public record ExceptionDetails(Integer statusCode, String message, Date timestamp) {}
