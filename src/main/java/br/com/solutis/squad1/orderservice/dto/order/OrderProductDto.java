package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Product;
import jakarta.validation.constraints.NotNull;

public record OrderProductDto(

        Long userId,

        String summary,

        Product product,

        Double totalPrice,

        Integer quantity
) {
}
