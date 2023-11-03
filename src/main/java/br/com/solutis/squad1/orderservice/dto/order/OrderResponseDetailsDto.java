package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDetailsDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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
