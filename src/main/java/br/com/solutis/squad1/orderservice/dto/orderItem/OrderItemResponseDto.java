package br.com.solutis.squad1.orderservice.dto.orderItem;

public record OrderItemResponseDto(
        Long id,
        Integer productId,
        Integer quantity,
        Double price,
        Double total
) {
}
