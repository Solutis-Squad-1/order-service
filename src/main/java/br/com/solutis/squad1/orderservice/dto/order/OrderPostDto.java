package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.entity.enums.Payment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record OrderPostDto(

        @NotNull
        Long userId,
        @NotNull
        Payment paymentId,
        @NotNull
        String summary,
        @NotEmpty
        List<Product> products
) {
}
