package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDetailsDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDetailsDto(
        Long id,
        Instant moment,
        Long userId,
        String summary,
        StatusPayment statusPayment,
        List<ProductResponseDetailsDto> products,
        BigDecimal totalPrice
) {
    public OrderResponseDetailsDto(Order order, List<ProductResponseDetailsDto> productResponseDetailsDto, BigDecimal totalPrice) {
        this(
                order.getId(),
                order.getMoment(),
                order.getUserId(),
                order.getSummary(),
                order.getStatusPayment(),
                productResponseDetailsDto,
                totalPrice
        );
    }
}
