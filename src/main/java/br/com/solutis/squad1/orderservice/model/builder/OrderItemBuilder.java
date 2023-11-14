package br.com.solutis.squad1.orderservice.model.builder;

import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class OrderItemBuilder {
    private Long id;
    private Order order;
    private Integer productId;
    private Integer quantity;
    private Double price;
    private Double total;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public OrderItemBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderItemBuilder order(Order order) {
        this.order = order;
        return this;
    }

    public OrderItemBuilder productId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public OrderItemBuilder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItemBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public OrderItemBuilder total(Double total) {
        this.total = total;
        return this;
    }

    public OrderItemBuilder deleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderItemBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OrderItemBuilder deletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderItem build(){
        return new OrderItem(id, order, productId, quantity, price, total, deleted, createdAt, deletedAt);
    }

    public OrderItemPostDto buildOrderItemPostDto() {
        return new OrderItemPostDto(productId, quantity, price);
    }

    public OrderItemResponseDto buildOrderItemResponseDto() {
        return new OrderItemResponseDto(id, productId, quantity, price, total);
    }
}
