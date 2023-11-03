package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;

import java.time.Instant;
import java.util.List;

public record OrderPutDto (
        Long userId,
        String summary,
        List<Product> products,
        StatusPayment statusPayment
) {
}
