package br.com.solutis.squad1.orderservice.model.entity;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderProductDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @Column(nullable = false)
    private Instant moment;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_payment", nullable = false)
    private StatusPayment statusPayment;

    @Column(nullable = false)
    private String summary;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderProduct> items = new HashSet<>();

    @Column(nullable = false)
    private boolean canceled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private LocalDateTime canceledAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        moment = Instant.now();
    }

    public void delete(Order order){
        canceled = true;
        canceledAt = LocalDateTime.now();
    }

    public void update(Order order){
        if (order.getUserId() != null) setUserId(order.getUserId());
        if (order.getSummary() != null) setSummary(order.getSummary());
        if (order.getStatusPayment() != null) setStatusPayment(order.getStatusPayment());
    }

    public Order(OrderProductDto orderProduct){
        this.userId = orderProduct.userId();
        this.statusPayment = StatusPayment.NOT_MADE;
        this.summary = orderProduct.summary();
    }

}
