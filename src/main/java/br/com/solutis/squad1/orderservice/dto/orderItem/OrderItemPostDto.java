package br.com.solutis.squad1.orderservice.dto.orderItem;

public record OrderItemPostDto(
        Integer productId,
        Integer quantity,
        Double price
) {
}
