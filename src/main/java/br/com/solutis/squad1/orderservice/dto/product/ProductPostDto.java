package br.com.solutis.squad1.orderservice.dto.product;

import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
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
