package br.com.solutis.squad1.orderservice.dto.product;

import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDetailsDto(
        Long id,
        String name,
        String description,
        Long sellerId,
        List<Category> categories,
        Image image,
        Integer quantity,
        BigDecimal price
) {
    public ProductResponseDetailsDto(Product product, Integer quantity, BigDecimal price){
        this (
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSellerId(),
                product.getCategories(),
                product.getImage(),
                quantity,
                price
        );
    }
}
