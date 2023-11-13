package br.com.solutis.squad1.orderservice.model.builder;

import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderBuilder {
    private Long id;
    private Long userId;
    private StatusPayment statusPayment;
    private String summary;
    private Set<OrderItem> items = new HashSet<>();
    private Double total;
    private boolean canceled = false;
    private LocalDateTime createdAt;
    private LocalDateTime canceledAt;

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public OrderBuilder statusPayment(StatusPayment statusPayment) {
        this.statusPayment = statusPayment;
        return this;
    }

    public OrderBuilder summary(String summary) {
        this.summary = summary;
        return this;
    }

    public OrderBuilder items(Set<OrderItem> items) {
        this.items.addAll(items);
        return this;
    }

    public OrderBuilder price(Double total) {
        this.total = total;
        return this;
    }

    public OrderBuilder canceled(boolean canceled) {
        this.canceled = canceled;
        return this;
    }

    public OrderBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OrderBuilder canceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
        return this;
    }

    public Order build() {
        return new Order(id, userId, statusPayment, summary, items, total, canceled, createdAt, canceledAt);
    }
}
