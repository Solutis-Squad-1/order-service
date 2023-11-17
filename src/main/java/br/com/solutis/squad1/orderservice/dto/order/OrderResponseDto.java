package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Order response dto
 */
public record OrderResponseDto(
        Long id,
        Long userId,
        String summary,
        Double total,
        List<OrderItemResponseDto> items,
        StatusPayment statusPayment
) {
    public OrderResponseDto(Order order, List<OrderItemResponseDto> items) {
        this(
                order.getId(),
                order.getUserId(),
                order.getSummary(),
                order.getTotal(),
                items,
                order.getStatusPayment()
        );
    }
}
