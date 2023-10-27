package br.com.solutis.squad1.orderservice.dto.product;

import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDto(

        String name,
        String description,
        BigDecimal price,
        Long sellerId,
        List<Category> categories,
        Image image,
        int quantity
) {

    public ProductResponseDto(Product product, List<Category> categories, Image image) {
        this(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSellerId(),
                categories,
                image,
                product.getQuantity()
        );
    }
}
