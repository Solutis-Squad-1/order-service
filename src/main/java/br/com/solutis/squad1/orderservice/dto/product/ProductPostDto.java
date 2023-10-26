package br.com.solutis.squad1.orderservice.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductPostDto(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Long sellerId,
        List<CategoryResponseDto> categories,
        ImageResponseDto image,
        int quantity
) {
}
