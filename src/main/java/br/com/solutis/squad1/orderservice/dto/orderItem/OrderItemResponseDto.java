package br.com.solutis.squad1.orderservice.dto.orderItem;

/**
 * Order item response dto
 */
public record OrderItemResponseDto(
        Long id,
        Integer productId,
        Integer quantity,
        Double price,
        Double total
) {
}
