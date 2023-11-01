package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.enums.Payment;
import java.math.BigDecimal;

public record OrderPaymentDto(
        Long id,
        BigDecimal total,
        Payment paymentId
) {
    public OrderPaymentDto(Order order, BigDecimal total) {
        this(
                order.getId(),
                total,
                order.getPaymentId()
        );
    }
}
