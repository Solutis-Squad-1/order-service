package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderPostDto(
        @NotNull
        Long userId,
        @NotNull
        String summary,
        @NotNull
        List<OrderItemPostDto> items
) {
}
