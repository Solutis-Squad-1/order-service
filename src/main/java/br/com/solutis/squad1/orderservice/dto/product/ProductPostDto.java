package br.com.solutis.squad1.orderservice.dto.product;

import br.com.solutis.squad1.orderservice.dto.order.OrderProductDto;
import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductPostDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long sellerId,
        List<CategoryResponseDto> categories,
        ImageResponseDto image
) {
    public ProductPostDto(Product product, List<CategoryResponseDto> categories, ImageResponseDto image){
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSellerId(),
                categories,
                image
        );
    }
}
