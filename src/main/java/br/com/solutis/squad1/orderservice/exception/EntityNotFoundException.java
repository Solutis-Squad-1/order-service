package br.com.solutis.squad1.orderservice.exception;

import lombok.Getter;

/**
 * This class represents a custom exception that is thrown when an entity is not found.
 * It extends the RuntimeException class, meaning it's an unchecked exception.
 */
@Getter
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
