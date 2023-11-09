package br.com.solutis.squad1.orderservice.model.entity;

import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_payment", nullable = false)
    private StatusPayment statusPayment = StatusPayment.NOT_PAID;

    @Column(nullable = false)
    private String summary;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> items = new HashSet<>();

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(nullable = false)
    private boolean canceled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void delete() {
        canceled = true;
        canceledAt = LocalDateTime.now();
        statusPayment = StatusPayment.CANCELED;
    }

    public void deleteItems() {
        items.forEach(OrderItem::delete);
    }

    public void update(Order order) {
        if (order.getSummary() != null) setSummary(order.getSummary());
        if (order.getStatusPayment() != null) setStatusPayment(order.getStatusPayment());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", statusPayment=" + statusPayment +
                ", summary='" + summary + '\'' +
                ", total=" + total +
                ", canceled=" + canceled +
                ", createdAt=" + createdAt +
                ", canceledAt=" + canceledAt +
                '}';
    }
}
