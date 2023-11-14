package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;

public record UpdateOrderStatusDto(
        Long id,
        StatusPayment status
) {
}
