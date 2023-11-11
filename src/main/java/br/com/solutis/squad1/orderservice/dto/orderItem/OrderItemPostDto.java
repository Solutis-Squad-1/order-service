package br.com.solutis.squad1.orderservice.dto.orderItem;

import jakarta.validation.constraints.NotNull;

public record OrderItemPostDto(
        @NotNull
        Integer productId,
        @NotNull
        Integer quantity,
        @NotNull
        Double price
) {
}
