package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderPostDto(

        @NotNull
        Long userId,
        @NotNull
        String summary,
        @NotEmpty
        List<Product> products
) {
}
