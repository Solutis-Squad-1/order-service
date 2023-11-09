package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;

import java.util.List;

public record OrderPutDto(
        String summary,
        List<OrderItemPostDto> items,
        StatusPayment statusPayment
) {
}
