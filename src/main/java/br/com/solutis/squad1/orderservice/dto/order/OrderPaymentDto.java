package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Order;

import java.math.BigDecimal;

public record OrderPaymentDto(
        Long id,
        BigDecimal total
) {
    public OrderPaymentDto(Order order, BigDecimal total) {
        this(
                order.getId(),
                total
        );
    }
}
