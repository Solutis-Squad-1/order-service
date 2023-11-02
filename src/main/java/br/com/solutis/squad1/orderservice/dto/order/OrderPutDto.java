package br.com.solutis.squad1.orderservice.dto.order;

import br.com.solutis.squad1.orderservice.model.entity.Product;

import java.time.Instant;
import java.util.List;

public record OrderPutDto (
        Instant moment,
        Long userId,
        String summary,
        List<Product> products
) {
}
