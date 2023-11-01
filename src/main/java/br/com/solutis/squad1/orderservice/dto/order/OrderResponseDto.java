package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.entity.enums.Payment;

import java.time.Instant;
import java.util.List;

public record OrderResponseDto(

        Long id,
        Instant moment,
        Long userId,
        Payment paymentId,
        String summary,
        List<Product> products
) {

    public OrderResponseDto(Order order, List<Product> products) {
        this(
                order.getId(),
                order.getMoment(),
                order.getUserId(),
                order.getPaymentId(),
                order.getSummary(),
                products
        );
    }
}
