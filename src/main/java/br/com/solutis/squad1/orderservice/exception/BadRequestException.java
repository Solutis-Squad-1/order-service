package br.com.solutis.squad1.orderservice.exception;

import lombok.Getter;

/**
 * Custom exception class for handling bad requests.
 * This class extends the RuntimeException class in Java.
 */
@Getter
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
